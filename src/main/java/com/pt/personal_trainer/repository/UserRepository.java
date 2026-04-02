package com.pt.personal_trainer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.pt.personal_trainer.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.username = :username WHERE u.id = :id")
    void updateUsernameById(Long id, String username);

    @Modifying
    @Query("UPDATE User u SET u.status = 0 WHERE u.id = :id")
    void updateStatusById(Long id);

    @Query("SELECT u.genderId FROM User u WHERE u.id = :id")
    Optional<Integer> findGenderIdById(Long id);

    @Query("SELECT u FROM User u WHERE u.emailVerified = false AND u.status = 1")
    List<User> findUnverifiedActiveUsers();

    @Modifying
    @Query("UPDATE User u SET u.emailVerified = true WHERE u.id = :id")
    void markEmailVerified(Long id);

}
