package com.pt.personal_trainer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pt.personal_trainer.entity.GoalMacroConfig;

@Repository
public interface GoalMacroConfigRepository extends JpaRepository<GoalMacroConfig, Integer> {

    Optional<GoalMacroConfig> findByGoalTypeId(Integer goalTypeId);

}
