package com.pt.personal_trainer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pt.personal_trainer.auth.JwtAuthFilter;
import com.pt.personal_trainer.auth.JwtUtil;
import com.pt.personal_trainer.auth.UserDetailsServiceImpl;
import com.pt.personal_trainer.domain.dto.AuthResponseDto;
import com.pt.personal_trainer.domain.dto.UserResponseDto;
import com.pt.personal_trainer.domain.input.LoginInput;
import com.pt.personal_trainer.domain.input.UserInput;
import com.pt.personal_trainer.service.AuthService;
import com.pt.personal_trainer.service.EmailConfirmationService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private EmailConfirmationService emailConfirmationService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void login_shouldReturnToken() throws Exception {
        LoginInput input = new LoginInput("john@test.com", "password123");
        UserResponseDto userDto = new UserResponseDto(1L, "john", "john@test.com", 1, true);
        AuthResponseDto response = new AuthResponseDto("jwt-token", Instant.now().plusMillis(86400000), userDto);

        when(authService.login(any(LoginInput.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("jwt-token"))
            .andExpect(jsonPath("$.user.email").value("john@test.com"));
    }

    @Test
    void login_shouldReturn400_whenInvalidInput() throws Exception {
        LoginInput input = new LoginInput("", "");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void register_shouldReturn201() throws Exception {
        UserInput input = new UserInput("john", "john@test.com", "password123", 1);
        UserResponseDto userDto = new UserResponseDto(1L, "john", "john@test.com", 1, false);
        AuthResponseDto response = new AuthResponseDto("jwt-token", Instant.now().plusMillis(86400000), userDto);

        when(authService.register(any(UserInput.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.token").value("jwt-token"))
            .andExpect(jsonPath("$.user.username").value("john"));
    }

    @Test
    void register_shouldReturn400_whenPasswordTooShort() throws Exception {
        UserInput input = new UserInput("john", "john@test.com", "short", 1);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void confirmEmail_shouldReturnHtml() throws Exception {
        doNothing().when(emailConfirmationService).confirmEmail("valid-token");

        mockMvc.perform(get("/api/auth/confirm-email").param("token", "valid-token"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Email Confirmed")));
    }
}
