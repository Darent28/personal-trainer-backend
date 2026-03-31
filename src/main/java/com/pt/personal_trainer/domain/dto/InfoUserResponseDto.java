package com.pt.personal_trainer.domain.dto;

import com.pt.personal_trainer.entity.InfoUser;

public record InfoUserResponseDto(
    Long id,
    Double weight,
    Double height,
    Double fatPercentage,
    Integer age,
    Integer activityLevel,
    Integer goal,
    Long userId
) {

    public static InfoUserResponseDto fromEntity(InfoUser infoUser) {
        return new InfoUserResponseDto(
            infoUser.getId(),
            infoUser.getWeight(),
            infoUser.getHeight(),
            infoUser.getFatPercentage(),
            infoUser.getAge(),
            infoUser.getActivityLevel(),
            infoUser.getGoal(),
            infoUser.getUserId()
        );
    }

}
