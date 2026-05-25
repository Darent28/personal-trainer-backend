package com.pt.personal_trainer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pt.personal_trainer.entity.PlicometryCheck;

@Repository
public interface PlicometryCheckRepository extends JpaRepository<PlicometryCheck, Long> {

    List<PlicometryCheck> findByUserIdOrderByCreatedAtDesc(Long userId);
}
