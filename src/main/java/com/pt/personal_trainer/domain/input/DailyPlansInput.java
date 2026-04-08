package com.pt.personal_trainer.domain.input;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyPlansInput {

    @NotNull(message = "Calories is required")
    private Integer totalCalories;

    @NotNull(message = "Carbohydrates is required")
    private Integer totalCarbs;

    @NotNull(message = "Proteins is required")
    private Integer totalProteins;

    @NotNull(message = "Fats is required")
    private Integer totalFats;

    @NotNull(message = "User info id is required")
    private Long userInfoId;
}
