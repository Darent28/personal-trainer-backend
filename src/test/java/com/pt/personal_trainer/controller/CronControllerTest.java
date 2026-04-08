package com.pt.personal_trainer.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.pt.personal_trainer.auth.JwtAuthFilter;
import com.pt.personal_trainer.auth.JwtUtil;
import com.pt.personal_trainer.auth.UserDetailsServiceImpl;
import com.pt.personal_trainer.config.AppProperties;
import com.pt.personal_trainer.service.EmailConfirmationService;

@WebMvcTest(CronController.class)
@AutoConfigureMockMvc(addFilters = false)
class CronControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmailConfirmationService emailConfirmationService;

    @MockitoBean
    private AppProperties appProperties;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void sendConfirmationEmails_shouldReturn200_whenSecretValid() throws Exception {
        when(appProperties.getCronSecret()).thenReturn("test-secret");
        when(emailConfirmationService.processUnverifiedUsers()).thenReturn(3);

        mockMvc.perform(post("/api/cron/send-confirmation-emails")
                .header("X-Cron-Secret", "test-secret"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.emailsSent").value(3));
    }

    @Test
    void sendConfirmationEmails_shouldReturn401_whenSecretInvalid() throws Exception {
        when(appProperties.getCronSecret()).thenReturn("test-secret");

        mockMvc.perform(post("/api/cron/send-confirmation-emails")
                .header("X-Cron-Secret", "wrong-secret"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error").value("Invalid cron secret"));
    }
}
