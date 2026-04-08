package com.pt.personal_trainer.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "daily_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public DailyPlans(Integer totalCalories, Integer totalProteins, Integer totalFats, Integer totalCarbs, Long userInfoId) {
        this.totalCalories = totalCalories;
        this.totalProteins = totalProteins;
        this.totalFats = totalFats;
        this.totalCarbs = totalCarbs;
        this.userInfoId = userInfoId;
    }
}
