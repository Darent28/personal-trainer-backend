package com.pt.personal_trainer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "usda")
@Getter
@Setter
public class UsdaProperties {

    private String apiKey = "DEMO_KEY";
    private String baseUrl = "https://api.nal.usda.gov/fdc/v1";
}
