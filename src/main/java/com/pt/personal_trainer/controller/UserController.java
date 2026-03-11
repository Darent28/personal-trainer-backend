package com.pt.personal_trainer.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.pt.personal_trainer.domain.UserInput;
import com.pt.personal_trainer.dto.UserResponseDto;
import com.pt.personal_trainer.service.UserService;
import jakarta.validation.Valid;

import java.util.List;


@RestController
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/user")
    public UserResponseDto postUser(@Valid @RequestBody UserInput userInput) {
        return userService.postUser(userInput);
    }

    @GetMapping("/user")
    public List<UserResponseDto> getUser() {
        return userService.getUser();
    }
    
    
}
