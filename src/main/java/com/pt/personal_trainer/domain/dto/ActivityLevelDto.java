package com.pt.personal_trainer.domain.dto;

import com.pt.personal_trainer.entity.LevelActivityType;

import lombok.Builder;

@Builder
public record ActivityLevelDto(
    Integer id,
    String activityName,
    Double factor
) {
    public static ActivityLevelDto fromEntity(LevelActivityType entity) {
        return ActivityLevelDto.builder()
            .id(entity.getId())
            .activityName(entity.getActivityName())
            .factor(entity.getFactor())
            .build();
    }
}
