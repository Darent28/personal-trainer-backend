package com.pt.personal_trainer.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietInput {
    
    @NotNull(message = "Calories is required")
    private Integer calories;

    @NotNull(message = "Carbohydrates is required")
    private Integer carbohydrates;

    @NotNull(message = "Proteins is required")
    private Integer proteins;
    
    @NotNull(message = "Fats is required")
    private Integer fats;
}
