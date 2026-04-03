package com.pt.personal_trainer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pt.personal_trainer.entity.GoalType;

@Repository
public interface GoalTypeRepository extends JpaRepository<GoalType, Integer> {

}
