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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



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

    @GetMapping("/users")
    public List<UserResponseDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/user/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/user/{id}")
    public UserResponseDto putMethodName(@PathVariable Long id, @RequestBody UserInput userInput) {
        return userService.updateUsername(id, userInput);
    }
    
    @PutMapping("/delete-user/{id}")
    public UserResponseDto deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
    
    
}
