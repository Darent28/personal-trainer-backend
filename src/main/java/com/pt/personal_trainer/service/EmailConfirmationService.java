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

        try {
            emailService.sendHtmlEmail(user.getEmail(), "Confirm your email - Personal Trainer", html);
        } catch (Exception e) {
            // Don't roll back the token - email failure is not critical
        }
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
            <p>Please confirm your email to activate your account.</p>
            <a href="%s"
               style="display: inline-block; margin-top: 20px; padding: 12px 28px;
                      background-color: #4CAF50; color: #ffffff; text-decoration: none;
                      border-radius: 6px; font-weight: bold;">
                Confirm Email
            </a>
            <p style="color: #888; font-size: 12px; margin-top: 30px;">
                If the button doesn't work, copy and paste this link into your browser:<br>
                <a href="%s">%s</a>
            </p>
            </body></html>
            """.formatted(username, confirmUrl, confirmUrl, confirmUrl);
    }
}
