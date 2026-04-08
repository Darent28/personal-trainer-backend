package com.pt.personal_trainer.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pt.personal_trainer.config.AppProperties;
import com.pt.personal_trainer.service.EmailConfirmationService;
import com.pt.personal_trainer.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cron")
@RequiredArgsConstructor
public class CronController {

    private final EmailConfirmationService emailConfirmationService;
    private final EmailService emailService;
    private final AppProperties appProperties;

    @PostMapping("/send-confirmation-emails")
    public ResponseEntity<Map<String, Object>> sendConfirmationEmails(
            @RequestHeader("X-Cron-Secret") String cronSecret) {

        if (!appProperties.getCronSecret().equals(cronSecret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid cron secret"));
        }

        int sent = emailConfirmationService.processUnverifiedUsers();
        return ResponseEntity.ok(Map.of("emailsSent", sent));
    }

    @PostMapping("/test-email")
    public ResponseEntity<Map<String, String>> testEmail(
            @RequestHeader("X-Cron-Secret") String cronSecret,
            @RequestParam String to) {

        if (!appProperties.getCronSecret().equals(cronSecret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid cron secret"));
        }

        String result = emailService.sendTestEmail(to);
        return ResponseEntity.ok(Map.of("result", result));
    }
}
