package com.pt.personal_trainer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pt.personal_trainer.entity.User;
import com.pt.personal_trainer.repository.UserRepository;
import com.pt.personal_trainer.domain.UserInput;
import com.pt.personal_trainer.dto.UserResponseDto;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDto postUser(UserInput userInput) {
        User user = new User(userInput.getUsername(), userInput.getEmail(), userInput.getPassword());
        userRepository.save(user);

        return UserResponseDto.fromEntity(user);
    }

    public List<UserResponseDto> getUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
            .map(UserResponseDto::fromEntity)
            .toList();
    }

}
