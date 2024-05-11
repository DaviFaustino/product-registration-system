package com.davifaustino.productregistrationsystem.business.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davifaustino.productregistrationsystem.business.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    
    ArrayList<Product> findByCodeContaining(String fragment);
}
