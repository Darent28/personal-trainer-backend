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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto postUser(UserInput userInput) {
        try {
            User user = new User(userInput.getUsername(), userInput.getEmail(), passwordEncoder.encode(userInput.getPassword()), userInput.getGenderId());
            userRepository.save(user);
            return UserResponseDto.fromEntity(user);
        } catch (Exception e) {
            log.error("Failed to create user", e);
            throw new ServerErrorException("Failed to create user. Please try again later.");
        }
    }

    public List<UserResponseDto> getUsers() {
        try {
            return userRepository.findAll().stream()
                .map(UserResponseDto::fromEntity)
                .toList();
        } catch (Exception e) {
            log.error("Failed to retrieve users", e);
            throw new ServerErrorException("Failed to retrieve users. Please try again later.");
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
            log.error("Failed to update user id={}", id, e);
            throw new ProcessServiceException("Failed to update user. Please try again later.", e);
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
            log.error("Failed to delete user id={}", id, e);
            throw new ProcessServiceException("Failed to delete user. Please try again later.", e);
        }
    }

}
