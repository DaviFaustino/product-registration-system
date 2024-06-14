package com.davifaustino.productregistrationsystem.business.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.davifaustino.productregistrationsystem.business.entities.EnumCategory;
import com.davifaustino.productregistrationsystem.business.entities.ProductType;

import jakarta.transaction.Transactional;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, String> {

    List<ProductType> findByNameIgnoreCaseContaining(String searchTerm);

    List<ProductType> findByNameIgnoreCaseContainingAndCategory(String searchTerm, EnumCategory category);

    @Query(value = "SELECT name from tb_product_types"
                + " ORDER BY name ASC;",
        nativeQuery = true)
    List<String> findAllProductTypeNames();

    @Modifying
    @Transactional
    @Query(value = "UPDATE tb_product_types SET name = :#{#productTypeUpdates.name}, " +
                   "category = :#{#productTypeUpdates.category.toString()}, " +
                   "full_stock_factor = :#{#productTypeUpdates.fullStockFactor} " +
                   "WHERE name = :name", nativeQuery = true)
    int updateByName(@Param("name") String name, @Param("productTypeUpdates") ProductType productTypeUpdates);
}
