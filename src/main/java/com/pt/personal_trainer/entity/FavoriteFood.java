package com.pt.personal_trainer.entity;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "favorite_foods", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "fdc_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteFood implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "fdc_id", nullable = false)
    private Integer fdcId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "brand")
    private String brand;

    @Column(name = "data_type")
    private String dataType;

    @Column(name = "calories")
    private Double calories;

    @Column(name = "protein_g")
    private Double proteinG;

    @Column(name = "carbs_g")
    private Double carbsG;

    @Column(name = "fat_g")
    private Double fatG;

    @Column(name = "fiber_g")
    private Double fiberG;

    @Column(name = "vitamin_a_ug")
    private Double vitaminAUg;

    @Column(name = "vitamin_c_mg")
    private Double vitaminCMg;

    @Column(name = "vitamin_d_ug")
    private Double vitaminDUg;

    @Column(name = "vitamin_e_mg")
    private Double vitaminEMg;

    @Column(name = "vitamin_k_ug")
    private Double vitaminKUg;

    @Column(name = "vitamin_b6_mg")
    private Double vitaminB6Mg;

    @Column(name = "vitamin_b12_ug")
    private Double vitaminB12Ug;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
}
