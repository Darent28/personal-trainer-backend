package com.pt.personal_trainer.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsdaNutrient(
        Integer nutrientId,
        String nutrientName,
        String unitName,
        Double value
) {}
