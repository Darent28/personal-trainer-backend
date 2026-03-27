package com.pt.personal_trainer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pt.personal_trainer.entity.User;
import com.pt.personal_trainer.repository.UserRepository;
import com.pt.personal_trainer.domain.dto.UserResponseDto;
import com.pt.personal_trainer.domain.input.UserInput;
import com.pt.personal_trainer.exception.CustomExceptions.NotFoundException;
import com.pt.personal_trainer.exception.CustomExceptions.ProcessServiceException;
import com.pt.personal_trainer.exception.CustomExceptions.ServerErrorException;

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
        try {
            User user = new User(userInput.getUsername(), userInput.getEmail(), passwordEncoder.encode(userInput.getPassword()), userInput.getGender_id());
            userRepository.save(user);
            return UserResponseDto.fromEntity(user);
        } catch (Exception e) {
            throw new ServerErrorException("Failed to create user: " + e.getMessage());
        }
    }

    public List<UserResponseDto> getUsers() {
        try {
            return userRepository.findAll().stream()
                .map(UserResponseDto::fromEntity)
                .toList();
        } catch (Exception e) {
            throw new ServerErrorException("Failed to retrieve users: " + e.getMessage());
        }
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return UserResponseDto.fromEntity(user);
    }

    @Transactional
    public UserResponseDto updateUsername(Long id, UserInput userInput) {
        userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        try {
            userRepository.updateUsernameById(id, userInput.getUsername());
            return UserResponseDto.fromEntity(userRepository.findById(id).get());
        } catch (Exception e) {
            throw new ProcessServiceException("Failed to update user: " + e.getMessage(), e);
        }
    }

    @Transactional
    public UserResponseDto deleteUser(Long id) {
        userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        try {
            userRepository.updateStatusById(id);
            return UserResponseDto.fromEntity(userRepository.findById(id).get());
        } catch (Exception e) {
            throw new ProcessServiceException("Failed to delete user: " + e.getMessage(), e);
        }
    }

}
