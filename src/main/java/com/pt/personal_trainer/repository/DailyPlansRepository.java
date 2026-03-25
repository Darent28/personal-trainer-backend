package com.pt.personal_trainer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pt.personal_trainer.entity.DailyPlans;

@Repository
public interface DailyPlansRepository extends JpaRepository<DailyPlans, Long> {

}
