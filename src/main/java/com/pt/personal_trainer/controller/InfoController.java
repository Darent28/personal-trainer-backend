package com.pt.personal_trainer.controller;

import org.springframework.web.bind.annotation.RestController;

import com.pt.personal_trainer.domain.dto.InfoUserResponseDto;
import com.pt.personal_trainer.domain.input.InfoUserInput;
import com.pt.personal_trainer.service.InfoUserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/api/info")
public class InfoController {
    
    private final InfoUserService infoUserService;

    public InfoController(InfoUserService infoUserService) {
        this.infoUserService = infoUserService;
    }

    @GetMapping("/get/{id}")
    public InfoUserResponseDto getInfoUserById(@PathVariable Long id) {
        return infoUserService.getInfoUserById(id);
    }

    @PostMapping("/create")
    public InfoUserResponseDto postInfoUser(@Valid @RequestBody InfoUserInput infoUserInput) {
        return infoUserService.postInfoUser(infoUserInput);
    }
    
}
