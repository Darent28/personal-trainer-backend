package com.pt.personal_trainer.domain.dto;

import java.time.Instant;

import com.pt.personal_trainer.entity.FavoriteFood;

public record FavoriteFoodDto(
        Long id,
        Integer fdcId,
        String name,
        String brand,
        String dataType,
        Double calories,
        Double proteinG,
        Double carbsG,
        Double fatG,
        Double fiberG,
        Vitamins vitamins,
        Instant createdAt
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

    public static FavoriteFoodDto fromEntity(FavoriteFood entity) {
        return new FavoriteFoodDto(
                entity.getId(),
                entity.getFdcId(),
                entity.getName(),
                entity.getBrand(),
                entity.getDataType(),
                entity.getCalories(),
                entity.getProteinG(),
                entity.getCarbsG(),
                entity.getFatG(),
                entity.getFiberG(),
                new Vitamins(
                        entity.getVitaminAUg(),
                        entity.getVitaminCMg(),
                        entity.getVitaminDUg(),
                        entity.getVitaminEMg(),
                        entity.getVitaminKUg(),
                        entity.getVitaminB6Mg(),
                        entity.getVitaminB12Ug()
                ),
                entity.getCreatedAt()
        );
    }
}
