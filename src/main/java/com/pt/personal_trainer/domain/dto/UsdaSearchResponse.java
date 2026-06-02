package com.pt.personal_trainer.domain.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsdaSearchResponse(
        Integer totalHits,
        Integer currentPage,
        Integer totalPages,
        List<UsdaFoodItem> foods
) {}
