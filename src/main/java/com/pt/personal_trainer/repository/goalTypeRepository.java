package com.pt.personal_trainer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pt.personal_trainer.entity.GoalType;

public interface GoalTypeRepository extends JpaRepository<GoalType, Integer> {

}
