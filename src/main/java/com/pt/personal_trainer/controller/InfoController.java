package com.pt.personal_trainer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pt.personal_trainer.domain.dto.DailyPlansDto;
import com.pt.personal_trainer.domain.dto.InfoUserResponseDto;
import com.pt.personal_trainer.domain.input.InfoUserInput;
import com.pt.personal_trainer.service.InfoUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/info")
public class InfoController {

    private final InfoUserService infoUserService;

    public InfoController(InfoUserService infoUserService) {
        this.infoUserService = infoUserService;
    }

    @GetMapping("/{id}")
    public InfoUserResponseDto getInfoUserById(@PathVariable Long id) {
        return infoUserService.getInfoUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InfoUserResponseDto postInfoUser(@Valid @RequestBody InfoUserInput infoUserInput) {
        return infoUserService.postInfoUser(infoUserInput);
    }

    @GetMapping("/{id}/daily-plan")
    public DailyPlansDto getDailyPlanByInfoId(@PathVariable Long id) {
        return infoUserService.getDailyPlanByInfoId(id);
    }

}
