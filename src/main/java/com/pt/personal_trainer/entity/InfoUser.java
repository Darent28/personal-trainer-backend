package com.pt.personal_trainer.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne()
    @JoinColumn(name = "activity_level", referencedColumnName = "id")
    private Integer activityLevel;

    @ManyToOne()
    @JoinColumn(name = "goal", referencedColumnName = "id")
    private Integer goal;

    public InfoUser(Double wheight, Double height, Double fatPorcentage, Integer age, Integer activityLevel, Integer goal) {
        this.wheight = wheight;
        this.height = height;
        this.fatPorcentage = fatPorcentage;
        this.age = age;
        this.activityLevel = activityLevel;
        this.goal = goal;
    }

}
