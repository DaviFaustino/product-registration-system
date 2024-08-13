package com.davifaustino.productregistrationsystem.api.dtos.requests;

import com.davifaustino.productregistrationsystem.api.validations.ProductCode;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {

    @Size(min = 4, max = 13)
    @ProductCode
    String code;

    @Size(min = 3, max = 32)
    String productTypeName;

    @Size(min = 3, max = 42)
    String name;

    @Size(max = 256)
    String description;

    Integer purchasePriceInCents;

    Integer salePriceInCents;

    Boolean fullStock;
}
