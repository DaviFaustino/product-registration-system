package com.davifaustino.productregistrationsystem.api.dtos;

import java.sql.Timestamp;

import com.davifaustino.productregistrationsystem.api.validations.ProductCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @Size(min = 4, max = 13)
    @ProductCode
    String code;

    @NotNull
    @Size(max = 32)
    String productTypeName;

    @NotNull
    @Size(min = 3, max = 42)
    String name;

    @Size(max = 256)
    String description;

    @NotNull
    Integer purchasePriceInCents;

    Integer previousPurchasePriceInCents;

    @NotNull
    Integer salePriceInCents;

    Integer previousSalePriceInCents;

    @NotNull
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    Timestamp priceUpdateDate;

    @NotNull
    Boolean fullStock;
}
