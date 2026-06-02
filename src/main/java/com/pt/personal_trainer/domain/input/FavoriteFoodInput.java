package com.pt.personal_trainer.domain.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteFoodInput {

    @NotNull(message = "fdcId is required")
    private Integer fdcId;

    @NotBlank(message = "name is required")
    private String name;

    private String brand;
    private String dataType;
    private Double calories;
    private Double proteinG;
    private Double carbsG;
    private Double fatG;
    private Double fiberG;
    private VitaminsInput vitamins;

    @Getter
    @Setter
    public static class VitaminsInput {
        private Double vitaminAUg;
        private Double vitaminCMg;
        private Double vitaminDUg;
        private Double vitaminEMg;
        private Double vitaminKUg;
        private Double vitaminB6Mg;
        private Double vitaminB12Ug;
    }
}
