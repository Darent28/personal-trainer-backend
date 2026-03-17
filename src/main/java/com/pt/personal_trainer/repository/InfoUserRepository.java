package com.pt.personal_trainer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pt.personal_trainer.entity.InfoUser;

@Repository
public interface InfoUserRepository extends JpaRepository<InfoUser, Long> {
    
}
