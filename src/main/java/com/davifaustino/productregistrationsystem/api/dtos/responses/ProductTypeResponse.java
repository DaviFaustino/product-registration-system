package com.davifaustino.productregistrationsystem.api.dtos.responses;

import com.davifaustino.productregistrationsystem.business.entities.EnumCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTypeResponse {

    String name;
    EnumCategory category;
    Integer averagePriceInCents;
    Short fullStockFactor;
}