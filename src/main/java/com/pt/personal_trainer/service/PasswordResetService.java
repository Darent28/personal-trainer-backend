package com.pt.personal_trainer.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pt.personal_trainer.config.AppProperties;
import com.pt.personal_trainer.entity.PasswordResetToken;
import com.pt.personal_trainer.entity.User;
import com.pt.personal_trainer.exception.CustomExceptions.ProcessServiceException;
import com.pt.personal_trainer.repository.PasswordResetTokenRepository;
import com.pt.personal_trainer.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;

    @Transactional
    public void sendResetEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ProcessServiceException("No account found for that email."));

        String tokenValue = UUID.randomUUID().toString();
        PasswordResetToken token = new PasswordResetToken(
            tokenValue,
            user,
            Instant.now().plus(appProperties.getPasswordResetTokenExpiryHours(), ChronoUnit.HOURS)
        );
        tokenRepository.save(token);

        String resetUrl = appProperties.getBaseUrl() + "/api/auth/reset-password?token=" + tokenValue;
        String html = buildResetHtml(user.getUsername(), resetUrl);

        try {
            emailService.sendHtmlEmail(user.getEmail(), "Reset your password - Personal Trainer", html);
        } catch (Exception e) {
            // Don't roll back the token - email failure is not critical
        }
    }

    @Transactional
    public void resetPassword(String tokenValue, String newPassword) {
        PasswordResetToken token = tokenRepository.findByToken(tokenValue)
            .orElseThrow(() -> new ProcessServiceException("Invalid reset token."));

        if (token.getUsed()) {
            throw new ProcessServiceException("This reset link has already been used.");
        }
        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new ProcessServiceException("This reset link has expired.");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        token.setUsed(true);
        tokenRepository.save(token);
    }

    private String buildResetHtml(String username, String resetUrl) {
        return """
            <!DOCTYPE html>
            <html><body style="font-family: Arial, sans-serif; text-align: center; padding: 50px;">
            <h2 style="color: #4CAF50;">Hi, %s!</h2>
            <p>We received a request to reset your password. Click the button below to choose a new one.</p>
            <a href="%s"
               style="display: inline-block; margin-top: 20px; padding: 12px 28px;
                      background-color: #4CAF50; color: #ffffff; text-decoration: none;
                      border-radius: 6px; font-weight: bold;">
                Reset Password
            </a>
            <p style="color: #888; font-size: 12px; margin-top: 30px;">
                If you didn't request this, you can safely ignore this email.<br>
                If the button doesn't work, copy and paste this link into your browser:<br>
                <a href="%s">%s</a>
            </p>
            </body></html>
            """.formatted(username, resetUrl, resetUrl, resetUrl);
    }
}
