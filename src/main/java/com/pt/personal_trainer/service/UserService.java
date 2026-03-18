package com.pt.personal_trainer.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.pt.personal_trainer.entity.User;
import com.pt.personal_trainer.repository.UserRepository;
import com.pt.personal_trainer.domain.UserInput;
import com.pt.personal_trainer.dto.UserResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDto postUser(UserInput userInput) {

        User user = new User(userInput.getUsername(), userInput.getEmail(), passwordEncoder.encode(userInput.getPassword()), userInput.getGender_id());
        userRepository.save(user);

        return UserResponseDto.fromEntity(user);
    }

    public List<UserResponseDto> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
            .map(UserResponseDto::fromEntity)
            .toList();
    }

    public UserResponseDto getUserById(Long id) {
        try {
            User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
            return UserResponseDto.fromEntity(user);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getReason());
        }
    }

    @Transactional
    public UserResponseDto updateUsername(Long id, UserInput userInput) {
             
        userRepository.updateUsernameById(id, userInput.getUsername());

        return UserResponseDto.fromEntity(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Transactional
    public UserResponseDto deleteUser(Long id) {
        userRepository.updateStatusById(id);
        return UserResponseDto.fromEntity(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

}
