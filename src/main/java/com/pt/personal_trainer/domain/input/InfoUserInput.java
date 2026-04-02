package com.pt.personal_trainer.domain.input;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InfoUserInput {

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be a positive number")
    private Double weight;

    @NotNull(message = "Height is required")
    @Positive(message = "Height must be a positive number")
    private Double height;

    @Min(value = 1, message = "Fat percentage must be at least 1")
    @Max(value = 70, message = "Fat percentage must be at most 70")
    private Double fatPercentage;

    @NotNull(message = "Age is required")
    @Min(value = 10, message = "Age must be at least 10")
    @Max(value = 100, message = "Age must be at most 100")
    private Integer age;

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Activity level is required")
    private Integer activityLevel;

    @NotNull(message = "Goal is required")
    private Integer goal;

}
