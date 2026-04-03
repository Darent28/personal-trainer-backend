package com.pt.personal_trainer.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pt.personal_trainer.domain.dto.ActivityLevelDto;
import com.pt.personal_trainer.domain.dto.GoalTypeDto;
import com.pt.personal_trainer.exception.CustomExceptions.ServerErrorException;
import com.pt.personal_trainer.repository.GoalTypeRepository;
import com.pt.personal_trainer.repository.LevelActivityTypeRepository;

@RestController
@RequestMapping("/api/lookup")
public class LookupController {

    private final GoalTypeRepository goalTypeRepository;
    private final LevelActivityTypeRepository levelActivityTypeRepository;

    public LookupController(GoalTypeRepository goalTypeRepository, LevelActivityTypeRepository levelActivityTypeRepository) {
        this.goalTypeRepository = goalTypeRepository;
        this.levelActivityTypeRepository = levelActivityTypeRepository;
    }

    @GetMapping("/goal-types")
    public List<GoalTypeDto> getGoalTypes() {
        try {
            return goalTypeRepository.findAll().stream()
                .map(GoalTypeDto::fromEntity)
                .toList();
        } catch (Exception e) {
            throw new ServerErrorException("Failed to retrieve goal types.");
        }
    }

    @GetMapping("/activity-levels")
    public List<ActivityLevelDto> getActivityLevels() {
        try {
            return levelActivityTypeRepository.findAll().stream()
                .map(ActivityLevelDto::fromEntity)
                .toList();
        } catch (Exception e) {
            throw new ServerErrorException("Failed to retrieve activity levels.");
        }
    }

}
