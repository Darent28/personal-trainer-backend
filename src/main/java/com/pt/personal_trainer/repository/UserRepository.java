package com.pt.personal_trainer.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pt.personal_trainer.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(String username);
    
    @Query("UPDATE User u SET u.username = :username WHERE u.id = :id")
    void updateUsernameById(Long id, String username);

    @Query("UPDATE User u SET u.status = 0 WHERE u.id = :id")
    void updateStatusById(Long id);

    @Query("SELECT u.gender_id FROM User u WHERE u.id = :id")
    Integer findGenderIdById(Long id);

}
