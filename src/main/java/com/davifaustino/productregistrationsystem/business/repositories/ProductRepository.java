package com.davifaustino.productregistrationsystem.business.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.davifaustino.productregistrationsystem.business.entities.Product;

import jakarta.transaction.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    
    ArrayList<Product> findByCodeContaining(String fragment);

    Optional<Product> findByCode(String code);

    List<Product> findByNameIgnoreCaseContaining(String name);

    List<Product> findByNameIgnoreCaseContainingAndProductTypeName(String name, String productTypeName);

    @Query(value = "SELECT sale_price_in_cents FROM tb_products " +
                    "WHERE product_type_name = :productTypeName", nativeQuery = true)
    List<Integer> findPricesByProductTypeName(@Param("productTypeName") String productTypeName);

    boolean existsByName(String name);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tb_products SET code = :#{#productUpdates.code}," +
                    "product_type_name = :#{#productUpdates.productTypeName}, " +
                    "name = :#{#productUpdates.name}, " +
                    "description = :#{#productUpdates.description}, " +
                    "purchase_price_in_cents = :#{#productUpdates.purchasePriceInCents}, " +
                    "sale_price_in_cents = :#{#productUpdates.salePriceInCents}, " +
                    "previous_purchase_price_in_cents = :#{#productUpdates.previousPurchasePriceInCents}, " +
                    "previous_sale_price_in_cents = :#{#productUpdates.previousSalePriceInCents}, " +
                    "price_update_date = :#{#productUpdates.priceUpdateDate}, " +
                    "full_stock = :#{#productUpdates.fullStock} " +
                    "WHERE code = :searchTerm", nativeQuery = true)
    int updateByCode(@Param("searchTerm") String code, @Param("productUpdates") Product productUpdates);
}
