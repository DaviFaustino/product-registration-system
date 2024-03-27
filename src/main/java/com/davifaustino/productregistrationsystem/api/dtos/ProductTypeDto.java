package com.davifaustino.productregistrationsystem.api.dtos;

import com.davifaustino.productregistrationsystem.entities.EnumCategories;

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

    EnumCategories category;

    Integer averagePriceInCents;

    @NotNull
    short fullStockFactor;
}