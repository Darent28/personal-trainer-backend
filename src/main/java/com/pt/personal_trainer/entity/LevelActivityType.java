package com.pt.personal_trainer.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "level_activity_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LevelActivityType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "factor")
    private Double factor;
}
