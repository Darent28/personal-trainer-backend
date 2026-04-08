package com.pt.personal_trainer.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "goal_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "goal_name")
    private String goalName;

    @Column(name = "goal_description")
    private String goalDescription;
}
