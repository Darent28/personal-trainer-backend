package com.pt.personal_trainer.auth;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.pt.personal_trainer.domain.dto.AuthResponseDto;
import com.pt.personal_trainer.domain.dto.UserResponseDto;
import com.pt.personal_trainer.domain.input.LoginInput;
import com.pt.personal_trainer.domain.input.UserInput;
import com.pt.personal_trainer.entity.User;
import com.pt.personal_trainer.exception.CustomExceptions.ProcessServiceException;
import com.pt.personal_trainer.repository.UserRepository;
import com.pt.personal_trainer.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Value("${jwt.expiration}")
    private long expirationMs;

    public AuthResponseDto login(LoginInput input) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new ProcessServiceException("Invalid email or password.");
        }

        User user = userRepository.findByEmail(input.getEmail())
            .orElseThrow(() -> new ProcessServiceException("User not found."));

        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getUsername());
        Instant expiresAt = Instant.now().plusMillis(expirationMs);

        return AuthResponseDto.builder()
            .token(token)
            .expiresAt(expiresAt)
            .user(UserResponseDto.fromEntity(user))
            .build();
    }

    public AuthResponseDto register(UserInput input) {
        UserResponseDto created = userService.postUser(input);

        User user = userRepository.findByEmail(input.getEmail())
            .orElseThrow(() -> new ProcessServiceException("Registration failed."));

        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getUsername());
        Instant expiresAt = Instant.now().plusMillis(expirationMs);

        return AuthResponseDto.builder()
            .token(token)
            .expiresAt(expiresAt)
            .user(created)
            .build();
    }

}
