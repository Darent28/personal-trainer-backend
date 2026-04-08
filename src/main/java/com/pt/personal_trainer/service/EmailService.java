package com.pt.personal_trainer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

    private final Resend resend;

    @Value("${resend.from}")
    private String fromAddress;

    public EmailService(@Value("${resend.api-key}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        log.info("Sending email to {} from {} via Resend", to, fromAddress);
        try {
            CreateEmailOptions options = CreateEmailOptions.builder()
                .from(fromAddress)
                .to(to)
                .subject(subject)
                .html(htmlBody)
                .build();

            CreateEmailResponse response = resend.emails().send(options);
            log.info("Email sent to {}. Id: {}", to, response.getId());
        } catch (ResendException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Email send failed: " + e.getMessage(), e);
        }
    }
}
