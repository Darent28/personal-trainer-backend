package com.pt.personal_trainer.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users_info")
public class InfoUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "_weight")
    private Double weight;

    @Column(name = "height")
    private Double height;

    @Column(name = "fat_percentage")
    private Double fatPercentage;

    @Column(name = "age")
    private Integer age;

    @Column(name = "activity_level")
    private Integer activityLevel;

    @Column(name = "goal")
    private Integer goal;

    @Column(name = "user_id")
    private Long userId;

    public InfoUser() {
    }

    public InfoUser(Long id, Double weight, Double height, Double fatPercentage, Integer age, Integer activityLevel, Integer goal, Long userId) {
        this.id = id;
        this.weight = weight;
        this.height = height;
        this.fatPercentage = fatPercentage;
        this.age = age;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.userId = userId;
    }

    public InfoUser(Double weight, Double height, Double fatPercentage, Integer age, Integer activityLevel, Integer goal, Long userId) {
        this.weight = weight;
        this.height = height;
        this.fatPercentage = fatPercentage;
        this.age = age;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
