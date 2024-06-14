package com.davifaustino.productregistrationsystem.api.dtos;

import com.davifaustino.productregistrationsystem.business.entities.EnumCategory;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTypeDto {

    @NotNull
    String name;

    EnumCategory category;

    Integer averagePriceInCents;

    @NotNull
    Short fullStockFactor;
}