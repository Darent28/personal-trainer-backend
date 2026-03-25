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
public class InfoUser implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "_wheight")
    private Double wheight;

    @Column(name = "height")
    private Double height;

    @Column(name = "fat_porcentage")
    private Double fatPorcentage;

    @Column(name = "age")
    private Integer age;

    @Column(name = "activity_level")
    private Integer activityLevel;

    @Column(name = "goal")
    private Integer goal;

    @Column(name = "user_id")
    private Long userId;

    public InfoUser(Double wheight, Double height, Double fatPorcentage, Integer age, Integer activityLevel, Integer goal, Long userId) {
        this.wheight = wheight;
        this.height = height;
        this.fatPorcentage = fatPorcentage;
        this.age = age;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.userId = userId;
    }

}
