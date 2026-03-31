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

}
