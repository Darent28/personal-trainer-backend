package com.pt.personal_trainer.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pt.personal_trainer.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
