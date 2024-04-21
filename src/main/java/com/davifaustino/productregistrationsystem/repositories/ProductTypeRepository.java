package com.davifaustino.productregistrationsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davifaustino.productregistrationsystem.entities.EnumCategory;
import com.davifaustino.productregistrationsystem.entities.ProductType;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, String> {

    List<ProductType> findByNameIgnoreCaseContaining(String searchTerm);

    List<ProductType> findByNameIgnoreCaseContainingAndCategory(String searchTerm, EnumCategory category);
}
