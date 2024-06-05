package com.davifaustino.productregistrationsystem.business.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.davifaustino.productregistrationsystem.business.entities.Product;
import com.davifaustino.productregistrationsystem.business.exceptions.InvalidSearchException;
import com.davifaustino.productregistrationsystem.business.exceptions.RecordConflictException;
import com.davifaustino.productregistrationsystem.business.repositories.ProductRepository;
import com.davifaustino.productregistrationsystem.business.repositories.ProductTypeRepository;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductTypeRepository productTypeRepository;

    public Product saveProduct(Product product) {
        if (product.getCode() == null) {
            product.setCode(getNewCode());
        }

        product.setCode(String.format("%13s", product.getCode()));

        if (productRepository.existsById(product.getCode())) {

            throw new RecordConflictException("The Product already exists in the database");
        }

        String errorMessage = "";

        if (!productTypeRepository.existsById(product.getProductTypeName())) {

            errorMessage = "The product type entered doesn't exist in the database";
        }
        if (productRepository.existsByName(product.getName())) {

            errorMessage = "The product name entered already exists in the database";
        }
        if (!errorMessage.equals("")) {

            throw new DataIntegrityViolationException(errorMessage);
        }

        product.setPriceUpdateDate(new Timestamp(System.currentTimeMillis()));

        return productRepository.save(product);
    }

    public List<Product> getProducts(String searchTerm, Optional<String> opProductTypeName) {
        
        if (searchTerm.matches("^[0-9]+$")) {
            
            if (searchTerm.length() > 2 && searchTerm.length() < 14) {
                Optional<Product> productOp = productRepository.findByCode(String.format("%13s", searchTerm));

                if (productOp.isPresent()) {
                    return Arrays.asList(productOp.get());
                }

                return new ArrayList<>();
            } else {
                throw new InvalidSearchException("Code size out of range");
            }
        }

        if (opProductTypeName.isPresent()) {

            return productRepository.findByNameIgnoreCaseContainingAndProductTypeName(searchTerm, opProductTypeName.get());
        }
        if (searchTerm.replace(" ", "").length() == 0) {

            throw new InvalidSearchException("No search parameters provided");
        }
        return productRepository.findByNameIgnoreCaseContaining(searchTerm);
    }

    private String getNewCode() {
        List<Product> productNewCode = productRepository.findByCodeContaining("          ");

        String lastCode = productNewCode.size() == 0 ? "000" : productNewCode.get(productNewCode.size() - 1).getCode();
        int newCode = Integer.parseInt(lastCode.replace(" ", "")) + 1;

        return String.format("%03d", newCode);
    }
}
