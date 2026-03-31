package com.pt.personal_trainer.domain.dto;

import com.pt.personal_trainer.entity.InfoUser;

import lombok.Builder;

@Builder
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
        return InfoUserResponseDto.builder()
            .id(infoUser.getId())
            .weight(infoUser.getWeight())
            .height(infoUser.getHeight())
            .fatPercentage(infoUser.getFatPercentage())
            .age(infoUser.getAge())
            .activityLevel(infoUser.getActivityLevel())
            .goal(infoUser.getGoal())
            .userId(infoUser.getUserId())
            .build();
    }

}
