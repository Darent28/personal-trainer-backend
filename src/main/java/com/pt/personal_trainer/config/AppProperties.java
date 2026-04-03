package com.pt.personal_trainer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String baseUrl;
    private String cronSecret;
    private int confirmationTokenExpiryHours;

    public AppProperties() {
    }

    public AppProperties(String baseUrl, String cronSecret, int confirmationTokenExpiryHours) {
        this.baseUrl = baseUrl;
        this.cronSecret = cronSecret;
        this.confirmationTokenExpiryHours = confirmationTokenExpiryHours;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getCronSecret() {
        return cronSecret;
    }

    public void setCronSecret(String cronSecret) {
        this.cronSecret = cronSecret;
    }

    public int getConfirmationTokenExpiryHours() {
        return confirmationTokenExpiryHours;
    }

    public void setConfirmationTokenExpiryHours(int confirmationTokenExpiryHours) {
        this.confirmationTokenExpiryHours = confirmationTokenExpiryHours;
    }
}
