package com.pt.personal_trainer.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pt.personal_trainer.domain.dto.FoodSearchResultDto;
import com.pt.personal_trainer.domain.dto.UsdaSearchResponse;
import com.pt.personal_trainer.exception.CustomExceptions.ProcessServiceException;
import com.pt.personal_trainer.external.usda.UsdaFoodClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

    private final UsdaFoodClient usdaFoodClient;

    @GetMapping("/search")
    public List<FoodSearchResultDto> searchFood(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int pageSize) {

        if (query == null || query.isBlank()) {
            throw new ProcessServiceException("Query parameter must not be blank.");
        }

        UsdaSearchResponse response = usdaFoodClient.searchFoods(query, pageSize);

        return FoodSearchResultDto.fromUsdaResponse(response.foods());
    }
}
