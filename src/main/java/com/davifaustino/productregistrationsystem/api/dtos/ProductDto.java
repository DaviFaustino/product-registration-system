package com.davifaustino.productregistrationsystem.api.dtos;

import java.sql.Timestamp;

import com.davifaustino.productregistrationsystem.api.validations.ProductCode;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @Max(value = 13)
    @Min(value = 4)
    @ProductCode
    String code;

    @NotNull
    @Max(value = 32)
    String productTypeName;

    @NotNull
    @Max(value = 42)
    String name;

    @Max(value = 256)
    String description;

    @NotNull
    Integer purchasePriceInCents;

    Integer previousPurchasePriceInCents;

    @NotNull
    Integer salePriceInCents;

    Integer previousSalePriceInCents;

    @NotNull
    Timestamp priceUpdateDate;

    @NotNull
    Boolean fullStock;
}
