package com.pt.personal_trainer.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pt.personal_trainer.domain.dto.FavoriteFoodDto;
import com.pt.personal_trainer.domain.dto.FoodSearchResultDto;
import com.pt.personal_trainer.domain.dto.UsdaSearchResponse;
import com.pt.personal_trainer.domain.input.FavoriteFoodInput;
import com.pt.personal_trainer.exception.CustomExceptions.ProcessServiceException;
import com.pt.personal_trainer.external.usda.UsdaFoodClient;
import com.pt.personal_trainer.service.FavoriteFoodService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

    private final UsdaFoodClient usdaFoodClient;
    private final FavoriteFoodService favoriteFoodService;

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

    @PostMapping("/favorites/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public FavoriteFoodDto addFavorite(
            @PathVariable Long userId,
            @Valid @RequestBody FavoriteFoodInput input) {

        return favoriteFoodService.addFavorite(userId, input);
    }

    @GetMapping("/favorites/{userId}")
    public List<FavoriteFoodDto> getFavorites(@PathVariable Long userId) {
        return favoriteFoodService.getFavorites(userId);
    }
}
