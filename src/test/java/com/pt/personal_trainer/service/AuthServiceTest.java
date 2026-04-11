package com.pt.personal_trainer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import com.pt.personal_trainer.auth.JwtUtil;
import com.pt.personal_trainer.config.JwtProperties;
import com.pt.personal_trainer.domain.dto.AuthResponseDto;
import com.pt.personal_trainer.domain.dto.UserResponseDto;
import com.pt.personal_trainer.domain.input.LoginInput;
import com.pt.personal_trainer.domain.input.UserInput;
import com.pt.personal_trainer.entity.User;
import com.pt.personal_trainer.exception.CustomExceptions.ProcessServiceException;
import com.pt.personal_trainer.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private JwtProperties jwtProperties;
    @Mock
    private EmailConfirmationService emailConfirmationService;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_shouldReturnToken_whenCredentialsValid() {
        LoginInput input = new LoginInput("john@test.com", "password");
        User user = new User("john", "john@test.com", "enc", 1);
        user.setId(1L);
        user.setEmailVerified(true);

        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("john@test.com", 1L, "john")).thenReturn("jwt-token");
        when(jwtProperties.getExpiration()).thenReturn(86400000L);

        AuthResponseDto result = authService.login(input);

        assertThat(result.token()).isEqualTo("jwt-token");
        assertThat(result.user().email()).isEqualTo("john@test.com");
    }

    @Test
    void login_shouldThrow_whenBadCredentials() {
        LoginInput input = new LoginInput("john@test.com", "wrong");
        when(authenticationManager.authenticate(any()))
            .thenThrow(new BadCredentialsException("bad"));

        assertThatThrownBy(() -> authService.login(input))
            .isInstanceOf(ProcessServiceException.class)
            .hasMessage("Invalid email or password.");
    }

    @Test
    void login_shouldThrow_whenEmailNotVerified() {
        LoginInput input = new LoginInput("john@test.com", "password");
        User user = new User("john", "john@test.com", "enc", 1);
        user.setId(1L);
        user.setEmailVerified(false);

        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.login(input))
            .isInstanceOf(ProcessServiceException.class)
            .hasMessage("Please confirm your email before logging in.");
    }

    @Test
    void register_shouldCreateUserAndSendEmail() {
        UserInput input = new UserInput("john", "john@test.com", "password123", 1);
        UserResponseDto dto = new UserResponseDto(1L, "john", "john@test.com", 1, false);
        User user = new User("john", "john@test.com", "enc", 1);
        user.setId(1L);

        when(userService.postUser(input)).thenReturn(dto);
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(user));

        UserResponseDto result = authService.register(input);

        assertThat(result.username()).isEqualTo("john");
        assertThat(result.email()).isEqualTo("john@test.com");
        verify(emailConfirmationService).sendConfirmationEmail(user);
    }
}
