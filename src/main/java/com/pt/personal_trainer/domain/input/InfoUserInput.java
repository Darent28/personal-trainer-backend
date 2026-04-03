package com.pt.personal_trainer.domain.input;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


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

    public InfoUserInput() {
    }

    public InfoUserInput(Double weight, Double height, Double fatPercentage, Integer age, Long userId, Integer activityLevel, Integer goal) {
        this.weight = weight;
        this.height = height;
        this.fatPercentage = fatPercentage;
        this.age = age;
        this.userId = userId;
        this.activityLevel = activityLevel;
        this.goal = goal;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getFatPercentage() {
        return fatPercentage;
    }

    public void setFatPercentage(Double fatPercentage) {
        this.fatPercentage = fatPercentage;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(Integer activityLevel) {
        this.activityLevel = activityLevel;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }
}
