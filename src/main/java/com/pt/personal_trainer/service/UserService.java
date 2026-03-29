package com.pt.personal_trainer.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
