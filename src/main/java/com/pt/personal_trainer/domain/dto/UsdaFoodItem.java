package com.pt.personal_trainer.domain.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsdaFoodItem(
        Integer fdcId,
        String description,
        String brandOwner,
        String dataType,
        List<UsdaNutrient> foodNutrients
) {}
