package com.pt.personal_trainer.domain.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record FoodSearchResultDto(
        Integer fdcId,
        String name,
        String brand,
        String dataType,
        Double calories,
        Double proteinG,
        Double fatG,
        Double fiberG,
        Vitamins vitamins
) {

    public record Vitamins(
            Double vitaminAUg,
            Double vitaminCMg,
            Double vitaminDUg,
            Double vitaminEMg,
            Double vitaminKUg,
            Double vitaminB6Mg,
            Double vitaminB12Ug
    ) {}

    public static FoodSearchResultDto fromUsdaItem(UsdaFoodItem item) {
        Map<Integer, Double> nutrients = item.foodNutrients() == null
                ? Map.of()
                : item.foodNutrients().stream()
                        .filter(n -> n.nutrientId() != null && n.value() != null)
                        .collect(Collectors.toMap(UsdaNutrient::nutrientId, UsdaNutrient::value, (a, b) -> a));

        Vitamins vitamins = new Vitamins(
                nutrients.get(1106),
                nutrients.get(1162),
                nutrients.get(1114),
                nutrients.get(1109),
                nutrients.get(1185),
                nutrients.get(1175),
                nutrients.get(1178)
        );

        return new FoodSearchResultDto(
                item.fdcId(),
                item.description(),
                item.brandOwner(),
                item.dataType(),
                nutrients.get(1008),
                nutrients.get(1003),
                nutrients.get(1004),
                nutrients.get(1079),
                vitamins
        );
    }

    public static List<FoodSearchResultDto> fromUsdaResponse(List<UsdaFoodItem> foods) {
        if (foods == null) return List.of();
        return foods.stream().map(FoodSearchResultDto::fromUsdaItem).toList();
    }
}
