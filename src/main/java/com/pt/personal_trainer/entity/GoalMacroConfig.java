package com.pt.personal_trainer.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "goal_macro_config")
public class GoalMacroConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "goal_type_id", nullable = false, unique = true)
    private Integer goalTypeId;

    // Calorie offset from TDEE (can be negative for cut, positive for bulk)
    @Column(name = "calorie_offset_min", nullable = false)
    private Integer calorieOffsetMin;

    @Column(name = "calorie_offset_max", nullable = false)
    private Integer calorieOffsetMax;

    // Protein range in grams per kg of bodyweight
    @Column(name = "protein_per_kg_min", nullable = false)
    private Double proteinPerKgMin;

    @Column(name = "protein_per_kg_max", nullable = false)
    private Double proteinPerKgMax;

    // Fat range in grams per kg of bodyweight
    @Column(name = "fat_per_kg_min", nullable = false)
    private Double fatPerKgMin;

    @Column(name = "fat_per_kg_max", nullable = false)
    private Double fatPerKgMax;

    public GoalMacroConfig() {
    }

    public GoalMacroConfig(Integer id, Integer goalTypeId, Integer calorieOffsetMin, Integer calorieOffsetMax, Double proteinPerKgMin, Double proteinPerKgMax, Double fatPerKgMin, Double fatPerKgMax) {
        this.id = id;
        this.goalTypeId = goalTypeId;
        this.calorieOffsetMin = calorieOffsetMin;
        this.calorieOffsetMax = calorieOffsetMax;
        this.proteinPerKgMin = proteinPerKgMin;
        this.proteinPerKgMax = proteinPerKgMax;
        this.fatPerKgMin = fatPerKgMin;
        this.fatPerKgMax = fatPerKgMax;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoalTypeId() {
        return goalTypeId;
    }

    public void setGoalTypeId(Integer goalTypeId) {
        this.goalTypeId = goalTypeId;
    }

    public Integer getCalorieOffsetMin() {
        return calorieOffsetMin;
    }

    public void setCalorieOffsetMin(Integer calorieOffsetMin) {
        this.calorieOffsetMin = calorieOffsetMin;
    }

    public Integer getCalorieOffsetMax() {
        return calorieOffsetMax;
    }

    public void setCalorieOffsetMax(Integer calorieOffsetMax) {
        this.calorieOffsetMax = calorieOffsetMax;
    }

    public Double getProteinPerKgMin() {
        return proteinPerKgMin;
    }

    public void setProteinPerKgMin(Double proteinPerKgMin) {
        this.proteinPerKgMin = proteinPerKgMin;
    }

    public Double getProteinPerKgMax() {
        return proteinPerKgMax;
    }

    public void setProteinPerKgMax(Double proteinPerKgMax) {
        this.proteinPerKgMax = proteinPerKgMax;
    }

    public Double getFatPerKgMin() {
        return fatPerKgMin;
    }

    public void setFatPerKgMin(Double fatPerKgMin) {
        this.fatPerKgMin = fatPerKgMin;
    }

    public Double getFatPerKgMax() {
        return fatPerKgMax;
    }

    public void setFatPerKgMax(Double fatPerKgMax) {
        this.fatPerKgMax = fatPerKgMax;
    }
}
