package com.davifaustino.productregistrationsystem.business.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davifaustino.productregistrationsystem.business.entities.EnumCategory;
import com.davifaustino.productregistrationsystem.business.entities.ProductType;
import com.davifaustino.productregistrationsystem.business.exceptions.RecordConflictException;
import com.davifaustino.productregistrationsystem.business.repositories.ProductTypeRepository;

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

    public List<ProductType> getProductTypes(String searchTerm, Optional<EnumCategory> opCategory) {
        if (opCategory.isPresent()) {

            return productTypeRepository.findByNameIgnoreCaseContainingAndCategory(searchTerm, opCategory.get());
        }
        return productTypeRepository.findByNameIgnoreCaseContaining(searchTerm);
    }
}
