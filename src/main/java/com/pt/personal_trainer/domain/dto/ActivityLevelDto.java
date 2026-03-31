package com.pt.personal_trainer.domain.dto;

import com.pt.personal_trainer.entity.LevelActivityType;

public record ActivityLevelDto(
    Integer id,
    String activityName,
    Double factor
) {
    public static ActivityLevelDto fromEntity(LevelActivityType entity) {
        return new ActivityLevelDto(
            entity.getId(),
            entity.getActivityName(),
            entity.getFactor()
        );
    }
}
