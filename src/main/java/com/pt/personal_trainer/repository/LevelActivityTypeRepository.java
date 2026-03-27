package com.pt.personal_trainer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pt.personal_trainer.entity.LevelActivityType;
@Repository
public interface LevelActivityTypeRepository extends JpaRepository<LevelActivityType, Integer> {

    @Query("SELECT la.factor FROM LevelActivityType la WHERE la.id = :id")
    Double findFactorById(Integer id);

}
