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

    public DailyPlans(Integer totalCalories, Integer totalProteins, Integer totalFats, Integer totalCarbs, Long userInfoId) {
        this.totalCalories = totalCalories;
        this.totalProteins = totalProteins;
        this.totalFats = totalFats;
        this.totalCarbs = totalCarbs;
        this.userInfoId = userInfoId;
    }

}
