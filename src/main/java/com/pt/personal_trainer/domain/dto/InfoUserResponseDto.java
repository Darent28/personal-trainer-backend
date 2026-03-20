package com.pt.personal_trainer.domain.dto;

import com.pt.personal_trainer.entity.InfoUser;

import lombok.Builder;

@Builder
public record InfoUserResponseDto(
    Long id,
    Double wheight,
    Double height,
    Double fatPorcentage,
    Integer age,
    Integer activityLevel,
    Integer goal
) {
    
   public static InfoUserResponseDto fromEntity(InfoUser infoUser) {

        
        return InfoUserResponseDto.builder()
            .id(infoUser.getId())
            .wheight(infoUser.getWheight())
            .height(infoUser.getHeight())
            .fatPorcentage(infoUser.getFatPorcentage())
            .age(infoUser.getAge())
            .activityLevel(infoUser.getActivityLevel())
            .goal(infoUser.getGoal() != null ? infoUser.getGoal().getId() : null)
            .build();

    }

}
