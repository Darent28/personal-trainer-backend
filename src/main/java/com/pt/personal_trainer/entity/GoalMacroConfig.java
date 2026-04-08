package com.pt.personal_trainer.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "goal_macro_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalMacroConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "goal_type_id", nullable = false, unique = true)
    private Integer goalTypeId;

    @Column(name = "calorie_offset_min", nullable = false)
    private Integer calorieOffsetMin;

    @Column(name = "calorie_offset_max", nullable = false)
    private Integer calorieOffsetMax;

    @Column(name = "protein_per_kg_min", nullable = false)
    private Double proteinPerKgMin;

    @Column(name = "protein_per_kg_max", nullable = false)
    private Double proteinPerKgMax;

    @Column(name = "fat_per_kg_min", nullable = false)
    private Double fatPerKgMin;

    @Column(name = "fat_per_kg_max", nullable = false)
    private Double fatPerKgMax;
}
