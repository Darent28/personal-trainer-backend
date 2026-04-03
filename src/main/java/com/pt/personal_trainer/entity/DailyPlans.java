package com.pt.personal_trainer.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "daily_plans")
public class DailyPlans implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "total_calories")
    private Integer totalCalories;

    @Column(name = "total_proteins")
    private Integer totalProteins;

    @Column(name = "total_fats")
    private Integer totalFats;

    @Column(name = "total_carbs")
    private Integer totalCarbs;

    @Column(name = "user_info_id")
    private Long userInfoId;

    public DailyPlans() {
    }

    public DailyPlans(Long id, Integer totalCalories, Integer totalProteins, Integer totalFats, Integer totalCarbs, Long userInfoId) {
        this.id = id;
        this.totalCalories = totalCalories;
        this.totalProteins = totalProteins;
        this.totalFats = totalFats;
        this.totalCarbs = totalCarbs;
        this.userInfoId = userInfoId;
    }

    public DailyPlans(Integer totalCalories, Integer totalProteins, Integer totalFats, Integer totalCarbs, Long userInfoId) {
        this.totalCalories = totalCalories;
        this.totalProteins = totalProteins;
        this.totalFats = totalFats;
        this.totalCarbs = totalCarbs;
        this.userInfoId = userInfoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(Integer totalCalories) {
        this.totalCalories = totalCalories;
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

    public Integer getTotalCarbs() {
        return totalCarbs;
    }

    public void setTotalCarbs(Integer totalCarbs) {
        this.totalCarbs = totalCarbs;
    }

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }
}
