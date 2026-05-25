package com.pt.personal_trainer.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.pt.personal_trainer.domain.dto.PlicometryCheckResponseDto;
import com.pt.personal_trainer.domain.input.PlicometryCheckInput;
import com.pt.personal_trainer.service.PlicometryCheckService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plicometry")
@RequiredArgsConstructor
public class PlicometryCheckController {

    private final PlicometryCheckService plicometryCheckService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlicometryCheckResponseDto save(@Valid @RequestBody PlicometryCheckInput input) {
        return plicometryCheckService.save(input);
    }

    @GetMapping("/user/{userId}")
    public List<PlicometryCheckResponseDto> getByUserId(@PathVariable Long userId) {
        return plicometryCheckService.getByUserId(userId);
    }
}
