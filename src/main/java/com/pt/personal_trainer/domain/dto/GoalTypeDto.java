package com.pt.personal_trainer.domain.dto;

import com.pt.personal_trainer.entity.GoalType;

public record GoalTypeDto(
    Integer id,
    String goalName,
    String goalDescription
) {
    public static GoalTypeDto fromEntity(GoalType entity) {
        return new GoalTypeDto(
            entity.getId(),
            entity.getGoalName(),
            entity.getGoalDescription()
        );
    }
}
