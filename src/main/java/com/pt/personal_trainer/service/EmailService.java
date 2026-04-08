package com.pt.personal_trainer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.from}")
    private String fromAddress;

    @Async
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        doSend(to, subject, htmlBody);
    }

    public String sendTestEmail(String to) {
        try {
            doSend(to, "Test Email - Personal Trainer", "<h1>Test email works!</h1>");
            return "Email sent successfully to " + to;
        } catch (Exception e) {
            return "FAILED: " + e.getMessage();
        }
    }

    private void doSend(String to, String subject, String htmlBody) {
        log.info("Attempting to send email to {} from {}", to, fromAddress);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
            log.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}. Error: {}", to, e.getMessage(), e);
            throw new RuntimeException("Email send failed: " + e.getMessage(), e);
        }
    }
}
