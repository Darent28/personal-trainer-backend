package com.pt.personal_trainer.domain.dto;

import com.pt.personal_trainer.entity.DailyPlans;

import lombok.Builder;

@Builder
public record DailyPlansDto(
    Long id,
    Integer totalCalories,
    Integer totalProteins,
    Integer totalFats,
    Integer totalCarbs,
    Long userInfoId
) {

    public static DailyPlansDto fromEntity(DailyPlans dailyPlans) {
        return DailyPlansDto.builder()
            .id(dailyPlans.getId())
            .totalCalories(dailyPlans.getTotalCalories())
            .totalProteins(dailyPlans.getTotalProteins())
            .totalFats(dailyPlans.getTotalFats())
            .totalCarbs(dailyPlans.getTotalCarbs())
            .userInfoId(dailyPlans.getUserInfoId())
            .build();
    }

}
