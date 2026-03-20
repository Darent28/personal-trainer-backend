package com.pt.personal_trainer.controller;

import org.springframework.web.bind.annotation.RestController;

import com.pt.personal_trainer.domain.dto.InfoUserResponseDto;
import com.pt.personal_trainer.domain.input.InfoUserInput;
import com.pt.personal_trainer.service.InfoUserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/info")
public class InfoController {
    
    private final InfoUserService infoUserService;

    public InfoController(InfoUserService infoUserService) {
        this.infoUserService = infoUserService;
    }

    @PostMapping("/create")
    public InfoUserResponseDto postInfoUser(@Valid @RequestBody InfoUserInput infoUserInput) {
        
        return infoUserService.postInfoUser(infoUserInput);
    }
    
}
