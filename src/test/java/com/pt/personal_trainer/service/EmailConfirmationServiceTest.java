package com.pt.personal_trainer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pt.personal_trainer.config.AppProperties;
import com.pt.personal_trainer.entity.EmailConfirmationToken;
import com.pt.personal_trainer.entity.User;
import com.pt.personal_trainer.exception.CustomExceptions.ProcessServiceException;
import com.pt.personal_trainer.repository.EmailConfirmationTokenRepository;
import com.pt.personal_trainer.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class EmailConfirmationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailConfirmationTokenRepository tokenRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private AppProperties appProperties;

    @InjectMocks
    private EmailConfirmationService emailConfirmationService;

    @Test
    void sendConfirmationEmail_shouldCreateTokenAndSendEmail() {
        User user = new User("john", "john@test.com", "enc", 1, LocalDate.of(2000, 1, 15));
        user.setId(1L);
        when(appProperties.getConfirmationTokenExpiryHours()).thenReturn(24);
        when(appProperties.getBaseUrl()).thenReturn("http://localhost:8080");

        emailConfirmationService.sendConfirmationEmail(user);

        verify(tokenRepository).save(any(EmailConfirmationToken.class));
        verify(emailService).sendHtmlEmail(eq("john@test.com"), any(), any());
    }

    @Test
    void confirmEmail_shouldMarkEmailVerified() {
        User user = new User("john", "john@test.com", "enc", 1, LocalDate.of(2000, 1, 15));
        user.setId(1L);
        EmailConfirmationToken token = new EmailConfirmationToken("valid-token", user,
            Instant.now().plus(1, ChronoUnit.HOURS));
        token.setUsed(false);

        when(tokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));

        emailConfirmationService.confirmEmail("valid-token");

        verify(userRepository).markEmailVerified(1L);
        assertThat(token.getUsed()).isTrue();
        verify(tokenRepository).save(token);
    }

    @Test
    void confirmEmail_shouldThrow_whenTokenInvalid() {
        when(tokenRepository.findByToken("bad-token")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> emailConfirmationService.confirmEmail("bad-token"))
            .isInstanceOf(ProcessServiceException.class)
            .hasMessage("Invalid confirmation token.");
    }

    @Test
    void confirmEmail_shouldThrow_whenTokenAlreadyUsed() {
        User user = new User("john", "john@test.com", "enc", 1, LocalDate.of(2000, 1, 15));
        user.setId(1L);
        EmailConfirmationToken token = new EmailConfirmationToken("used-token", user,
            Instant.now().plus(1, ChronoUnit.HOURS));
        token.setUsed(true);

        when(tokenRepository.findByToken("used-token")).thenReturn(Optional.of(token));

        assertThatThrownBy(() -> emailConfirmationService.confirmEmail("used-token"))
            .isInstanceOf(ProcessServiceException.class)
            .hasMessage("This confirmation link has already been used.");
    }

    @Test
    void confirmEmail_shouldThrow_whenTokenExpired() {
        User user = new User("john", "john@test.com", "enc", 1, LocalDate.of(2000, 1, 15));
        user.setId(1L);
        EmailConfirmationToken token = new EmailConfirmationToken("expired-token", user,
            Instant.now().minus(1, ChronoUnit.HOURS));
        token.setUsed(false);

        when(tokenRepository.findByToken("expired-token")).thenReturn(Optional.of(token));

        assertThatThrownBy(() -> emailConfirmationService.confirmEmail("expired-token"))
            .isInstanceOf(ProcessServiceException.class)
            .hasMessage("This confirmation link has expired.");
    }
}
