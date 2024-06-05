package com.davifaustino.productregistrationsystem.business.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davifaustino.productregistrationsystem.business.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    
    ArrayList<Product> findByCodeContaining(String fragment);

    Optional<Product> findByCode(String code);

    List<Product> findByNameIgnoreCaseContaining(String name);

    List<Product> findByNameIgnoreCaseContainingAndProductTypeName(String name, String productTypeName);

    boolean existsByName(String name);
}
