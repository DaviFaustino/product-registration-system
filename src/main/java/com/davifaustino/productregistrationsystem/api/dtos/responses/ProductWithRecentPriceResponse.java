package com.davifaustino.productregistrationsystem.api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithRecentPriceResponse {
    
    String name;
    Integer salePriceInCents;
    Integer previousSalePriceInCents;
}
