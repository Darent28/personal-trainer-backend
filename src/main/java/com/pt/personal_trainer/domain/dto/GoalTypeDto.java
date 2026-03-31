package com.pt.personal_trainer.domain.dto;

import com.pt.personal_trainer.entity.GoalType;

import lombok.Builder;

@Builder
public record GoalTypeDto(
    Integer id,
    String goalName,
    String goalDescription
) {
    public static GoalTypeDto fromEntity(GoalType entity) {
        return GoalTypeDto.builder()
            .id(entity.getId())
            .goalName(entity.getGoalName())
            .goalDescription(entity.getGoalDescription())
            .build();
    }
}
