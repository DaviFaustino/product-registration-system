package com.davifaustino.productregistrationsystem.business.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.davifaustino.productregistrationsystem.business.entities.EnumCategory;
import com.davifaustino.productregistrationsystem.business.entities.ProductType;
import com.davifaustino.productregistrationsystem.business.exceptions.NonExistingRecordException;
import com.davifaustino.productregistrationsystem.business.exceptions.RecordConflictException;
import com.davifaustino.productregistrationsystem.business.repositories.ProductRepository;
import com.davifaustino.productregistrationsystem.business.repositories.ProductTypeRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductTypeService {
    
    @Autowired
    ProductTypeRepository productTypeRepository;

    @Autowired
    ProductRepository productRepository;


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

    public List<String> getProductTypeNames() {
        return productTypeRepository.findAllProductTypeNames();
    }

    @SuppressWarnings("null")
    @Transactional
    public int updateProductType(String id, Map<String, Object> productTypeUpdates) {

        ProductType existingProductType = productTypeRepository.findById(id).orElseThrow(() -> new NonExistingRecordException("Product type not found"));
        ProductType updatedProductType = new ProductType();

        productTypeUpdates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(ProductType.class, key);
            field.setAccessible(true);

            if (value != null && value != "") {
                ReflectionUtils.setField(field, updatedProductType, value);
            } else {
                ReflectionUtils.setField(field, updatedProductType, ReflectionUtils.getField(field, existingProductType));
            }
        });
        
        return productTypeRepository.updateByName(id, updatedProductType);
    }

    @Async
    public void updateAveragePriceInCents(String productTypeName) {

        List<Integer> prices = productRepository.findPricesByProductTypeName(productTypeName);
        Integer averagePrice = prices.stream().mapToInt(Integer::intValue).sum() / prices.size();

        ProductType existingProductType = productTypeRepository.findById(productTypeName).orElse(null);
        existingProductType.setAveragePriceInCents(averagePrice);
        productTypeRepository.save(existingProductType);
    }

    @Transactional
    public void deleteProductType(String id) {
        if (!productTypeRepository.existsById(id)) {
            throw new NonExistingRecordException("Product type not found");
        }
        productTypeRepository.deleteById(id);
    }
}
