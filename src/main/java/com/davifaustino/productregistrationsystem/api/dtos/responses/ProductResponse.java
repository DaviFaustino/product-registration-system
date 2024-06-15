package com.davifaustino.productregistrationsystem.api.dtos.responses;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    String code;
    String productTypeName;
    String name;
    String description;
    Integer purchasePriceInCents;
    Integer previousPurchasePriceInCents;
    Integer salePriceInCents;
    Integer previousSalePriceInCents;
    Timestamp priceUpdateDate;
    Boolean fullStock;
}
