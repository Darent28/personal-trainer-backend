package com.pt.personal_trainer.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data
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

    public InfoUser(Double weight, Double height, Double fatPercentage, Integer age, Integer activityLevel, Integer goal, Long userId) {
        this.weight = weight;
        this.height = height;
        this.fatPercentage = fatPercentage;
        this.age = age;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.userId = userId;
    }

}
