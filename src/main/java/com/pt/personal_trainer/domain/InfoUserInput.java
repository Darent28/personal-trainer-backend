package com.pt.personal_trainer.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InfoUserInput {

    @NotNull(message = "Wheight is required")
    private Double wheight;
    
    @NotNull(message = "Height is required")
    private Double height;

    @NotNull(message = "Fat percentage is required")
    private Double fatPorcentage;

    @NotNull(message = "Age is required")
    private Integer age;

    @NotNull(message = "Activity level is required")
    private Integer activityLevel;

    @Pattern(regexp = "1|2|3", message = "Goal must be 1, 2, or 3")
    @NotNull(message = "Goal is required")
    private Integer goal;

}
