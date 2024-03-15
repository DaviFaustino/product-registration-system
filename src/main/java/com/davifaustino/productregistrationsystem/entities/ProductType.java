package com.davifaustino.productregistrationsystem.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    private EnumCategories category;

    @Column(name = "average_price")
    private int averagePrice;

    @Column(name = "full_stock_factor")
    private short fullStockFactor;
}
