package com.davifaustino.productregistrationsystem.business.entities;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String code;

    @Column(name = "product_type_name")
    private String productTypeName;
    
    private String name;

    private String description;

    @Column(name = "purchase_price_in_cents")
    private Integer purchasePriceInCents;

    @Column(name = "previous_purchase_price_in_cents")
    private Integer previousPurchasePriceInCents;

    @Column(name = "sale_price_in_cents")
    private Integer salePriceInCents;

    @Column(name = "previous_sale_price_in_cents")
    private Integer previousSalePriceInCents;

    @Column(name = "price_update_date")
    private Timestamp priceUpdateDate;

    @Column(name = "full_stock")
    private Boolean fullStock;
}
