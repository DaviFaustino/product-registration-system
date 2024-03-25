package com.davifaustino.productregistrationsystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davifaustino.productregistrationsystem.entities.ProductType;
import com.davifaustino.productregistrationsystem.exceptions.RecordConflictException;
import com.davifaustino.productregistrationsystem.repositories.ProductTypeRepository;

@Service
public class ProductTypeService {
    @Autowired
    ProductTypeRepository productTypeRepository;

    public ProductType saveProductType(ProductType productType) {
        if (productTypeRepository.existsById(productType.getName())) {

            throw new RecordConflictException("The Product Type already exists in the database");
        }
        return productTypeRepository.save(productType);
    }
}
