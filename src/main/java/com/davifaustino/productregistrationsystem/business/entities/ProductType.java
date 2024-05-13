package com.davifaustino.productregistrationsystem.business.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_product_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductType {
    
    @Id
    private String name;

    @Enumerated(EnumType.STRING)
    private EnumCategory category;

    @Column(name = "average_price_in_cents")
    private Integer averagePriceInCents;

    @Column(name = "full_stock_factor")
    private short fullStockFactor;
}
