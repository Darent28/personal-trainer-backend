package com.pt.personal_trainer.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pt.personal_trainer.entity.EmailConfirmationToken;

@Repository
public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, Long> {

    Optional<EmailConfirmationToken> findByToken(String token);

    @Query("SELECT t FROM EmailConfirmationToken t WHERE t.user.id = :userId AND t.used = false AND t.expiresAt > :now")
    Optional<EmailConfirmationToken> findValidTokenByUserId(Long userId, Instant now);
}
