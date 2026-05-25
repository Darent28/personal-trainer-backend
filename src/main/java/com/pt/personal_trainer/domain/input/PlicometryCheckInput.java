package com.pt.personal_trainer.domain.input;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlicometryCheckInput {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Method is required")
    @Min(value = 3, message = "Method must be 3 or 7")
    @Max(value = 7, message = "Method must be 3 or 7")
    private Integer method;

    @NotNull(message = "Fat percentage is required")
    @DecimalMin(value = "0.1", message = "Fat percentage must be greater than 0")
    @DecimalMax(value = "70.0", message = "Fat percentage must be at most 70")
    private Double fatPercentage;

    @DecimalMin(value = "0.1", message = "Site value must be greater than 0")
    private Double siteChest;

    @DecimalMin(value = "0.1", message = "Site value must be greater than 0")
    private Double siteMidaxillary;

    @DecimalMin(value = "0.1", message = "Site value must be greater than 0")
    private Double siteTriceps;

    @DecimalMin(value = "0.1", message = "Site value must be greater than 0")
    private Double siteSubscapular;

    @DecimalMin(value = "0.1", message = "Site value must be greater than 0")
    private Double siteAbdomen;

    @DecimalMin(value = "0.1", message = "Site value must be greater than 0")
    private Double siteSuprailiac;

    @DecimalMin(value = "0.1", message = "Site value must be greater than 0")
    private Double siteThigh;

    @DecimalMin(value = "0.1", message = "Site value must be greater than 0")
    private Double siteIliacCrest;
}
