package com.pt.personal_trainer.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pt.personal_trainer.config.AppProperties;
import com.pt.personal_trainer.service.EmailConfirmationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cron")
@RequiredArgsConstructor
public class CronController {

    private final EmailConfirmationService emailConfirmationService;
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
}
