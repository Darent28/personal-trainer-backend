package com.pt.personal_trainer.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
