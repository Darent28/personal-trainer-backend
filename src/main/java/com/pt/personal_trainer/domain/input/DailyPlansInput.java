package com.pt.personal_trainer.domain.input;

import jakarta.validation.constraints.NotNull;

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

    public DailyPlansInput() {
    }

    public DailyPlansInput(Integer totalCalories, Integer totalCarbs, Integer totalProteins, Integer totalFats, Long userInfoId) {
        this.totalCalories = totalCalories;
        this.totalCarbs = totalCarbs;
        this.totalProteins = totalProteins;
        this.totalFats = totalFats;
        this.userInfoId = userInfoId;
    }

    public Integer getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(Integer totalCalories) {
        this.totalCalories = totalCalories;
    }

    public Integer getTotalCarbs() {
        return totalCarbs;
    }

    public void setTotalCarbs(Integer totalCarbs) {
        this.totalCarbs = totalCarbs;
    }

    public Integer getTotalProteins() {
        return totalProteins;
    }

    public void setTotalProteins(Integer totalProteins) {
        this.totalProteins = totalProteins;
    }

    public Integer getTotalFats() {
        return totalFats;
    }

    public void setTotalFats(Integer totalFats) {
        this.totalFats = totalFats;
    }

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }
}
