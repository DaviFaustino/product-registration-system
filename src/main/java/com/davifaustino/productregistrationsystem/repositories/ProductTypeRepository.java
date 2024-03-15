package com.davifaustino.productregistrationsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davifaustino.productregistrationsystem.entities.ProductType;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, String> {

}
