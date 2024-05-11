package com.davifaustino.productregistrationsystem.business.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davifaustino.productregistrationsystem.business.entities.Product;
import com.davifaustino.productregistrationsystem.business.exceptions.RecordConflictException;
import com.davifaustino.productregistrationsystem.business.repositories.ProductRepository;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Product saveProduct(Product product) {
        if (product.getCode() == null) {
            product.setCode(getNewCode());
        }

        product.setCode(String.format("%13s", product.getCode()));

        if (productRepository.existsById(product.getCode())) {

            throw new RecordConflictException("The Product already exists in the database");
        }
        return productRepository.save(product);
    }

    private String getNewCode() {
        List<Product> productNewCode = productRepository.findByCodeContaining("          ");

        String lastCode = productNewCode.size() == 0 ? "000" : productNewCode.get(productNewCode.size() - 1).getCode();
        int newCode = Integer.parseInt(lastCode.replace(" ", "")) + 1;

        return String.format("%03d", newCode);
    }
}
