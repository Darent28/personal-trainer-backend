package com.pt.personal_trainer.domain.dto;

import com.pt.personal_trainer.entity.DailyPlans;

public record DailyPlansDto(
    Long id,
    Integer totalCalories,
    Integer totalProteins,
    Integer totalFats,
    Integer totalCarbs,
    Long userInfoId
) {

    public static DailyPlansDto fromEntity(DailyPlans dailyPlans) {
        return new DailyPlansDto(
            dailyPlans.getId(),
            dailyPlans.getTotalCalories(),
            dailyPlans.getTotalProteins(),
            dailyPlans.getTotalFats(),
            dailyPlans.getTotalCarbs(),
            dailyPlans.getUserInfoId()
        );
    }

}
