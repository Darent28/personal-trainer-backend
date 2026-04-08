package com.pt.personal_trainer.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pt.personal_trainer.config.AppProperties;
import com.pt.personal_trainer.entity.EmailConfirmationToken;
import com.pt.personal_trainer.entity.User;
import com.pt.personal_trainer.exception.CustomExceptions.ProcessServiceException;
import com.pt.personal_trainer.repository.EmailConfirmationTokenRepository;
import com.pt.personal_trainer.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailConfirmationService {

    private final UserRepository userRepository;
    private final EmailConfirmationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final AppProperties appProperties;

    @Transactional
    public int processUnverifiedUsers() {
        List<User> unverified = userRepository.findUnverifiedActiveUsers();
        int sent = 0;

        for (User user : unverified) {
            Optional<EmailConfirmationToken> existing = tokenRepository
                .findValidTokenByUserId(user.getId(), Instant.now());
            if (existing.isPresent()) continue;

            sendConfirmationEmail(user);
            sent++;
        }
        return sent;
    }

    @Transactional
    public void sendConfirmationEmail(User user) {
        String tokenValue = UUID.randomUUID().toString();
        EmailConfirmationToken token = new EmailConfirmationToken(
            tokenValue,
            user,
            Instant.now().plus(appProperties.getConfirmationTokenExpiryHours(), ChronoUnit.HOURS)
        );
        tokenRepository.save(token);

        String confirmUrl = appProperties.getBaseUrl() + "/api/auth/confirm-email?token=" + tokenValue;
        String html = buildConfirmationHtml(user.getUsername(), confirmUrl);

        emailService.sendHtmlEmail(user.getEmail(), "Confirm your email - Personal Trainer", html);
    }

    @Transactional
    public void confirmEmail(String tokenValue) {
        EmailConfirmationToken token = tokenRepository.findByToken(tokenValue)
            .orElseThrow(() -> new ProcessServiceException("Invalid confirmation token."));

        if (token.getUsed()) {
            throw new ProcessServiceException("This confirmation link has already been used.");
        }
        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new ProcessServiceException("This confirmation link has expired.");
        }

        userRepository.markEmailVerified(token.getUser().getId());
        token.setUsed(true);
        tokenRepository.save(token);
    }

    private String buildConfirmationHtml(String username, String confirmUrl) {
        return """
            <!DOCTYPE html>
            <html><body style="font-family: Arial, sans-serif; text-align: center; padding: 50px;">
            <h2 style="color: #4CAF50;">Welcome, %s!</h2>
            <p>Please confirm your email by <a href="%s">clicking here</a>.</p>
            </body></html>
            """.formatted(username, confirmUrl);
    }
}
