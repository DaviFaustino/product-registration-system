package com.davifaustino.productregistrationsystem.business.services;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.davifaustino.productregistrationsystem.business.entities.Product;
import com.davifaustino.productregistrationsystem.business.exceptions.InvalidSearchException;
import com.davifaustino.productregistrationsystem.business.exceptions.NonExistingRecordException;
import com.davifaustino.productregistrationsystem.business.exceptions.RecordConflictException;
import com.davifaustino.productregistrationsystem.business.exceptions.UpdatesNotProvidedException;
import com.davifaustino.productregistrationsystem.business.repositories.ProductRepository;
import com.davifaustino.productregistrationsystem.business.repositories.ProductTypeRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductTypeRepository productTypeRepository;

    @Autowired
    ProductTypeService productTypeService;


    public Product saveProduct(Product product, boolean isPriceOld) {
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

        if (isPriceOld) {
            product.setPriceUpdateDate(new Timestamp(0));
        } else {
            product.setPriceUpdateDate(new Timestamp(System.currentTimeMillis()));
        }
        Product savedProduct = productRepository.save(product);

        productTypeService.updateAveragePriceInCents(savedProduct.getProductTypeName());
        
        return savedProduct;
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

    @Transactional
    public int updateProduct(String id, Map<String, Object> productUpdates) {

        for (String key : productUpdates.keySet()) {
            if (productUpdates.get(key) != null) {
                break;
            }
            if (key.equals("fullStock")) {
                throw new UpdatesNotProvidedException("No update data provided");
            }
        }

        List<Product> existingProduct = getProducts(id, Optional.ofNullable(null));
        Product updatedProduct;
        if (existingProduct.size() != 0) {
            updatedProduct = composeUpdatedProduct(productUpdates, existingProduct.get(0));
        } else {
            throw new NonExistingRecordException("Product not found");
        }

        int modifiedLines = productRepository.updateByCode(String.format("%13s", id), updatedProduct);
        productTypeService.updateAveragePriceInCents(updatedProduct.getProductTypeName());

        return modifiedLines;
    }

    @Transactional
    public void deleteProduct(String id) {
        
        if (!productRepository.existsById(id)) {
            throw new NonExistingRecordException("Product not found");
        }
        productRepository.deleteById(id);
    }

    private String getNewCode() {
        List<Product> productNewCode = productRepository.findByCodeContaining("          ");

        String lastCode = productNewCode.size() == 0 ? "000" : productNewCode.get(productNewCode.size() - 1).getCode();
        int newCode = Integer.parseInt(lastCode.replace(" ", "")) + 1;

        return String.format("%03d", newCode);
    }

    @SuppressWarnings("null")
    public Product composeUpdatedProduct(Map<String, Object> productUpdates, Product existingProduct) {
        Product updatedProduct = new Product(existingProduct.getCode(), existingProduct.getProductTypeName(),
                                            existingProduct.getName(), existingProduct.getDescription(),
                                            existingProduct.getPurchasePriceInCents(), existingProduct.getPreviousPurchasePriceInCents(),
                                            existingProduct.getSalePriceInCents(), existingProduct.getPreviousSalePriceInCents(),
                                            existingProduct.getPriceUpdateDate(), existingProduct.getFullStock());

        productUpdates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Product.class, key);
            field.setAccessible(true);

            if (value != null && !value.equals(ReflectionUtils.getField(field, existingProduct))) {
                ReflectionUtils.setField(field, updatedProduct, value);

                if (field.getName().equals("purchasePriceInCents")) {
                    Field previousValueField = ReflectionUtils.findField(Product.class, "previousPurchasePriceInCents");
                    previousValueField.setAccessible(true);

                    ReflectionUtils.setField(previousValueField, updatedProduct, ReflectionUtils.getField(field, existingProduct));
                }
                if (field.getName().equals("salePriceInCents")) {
                    Field previousValueField = ReflectionUtils.findField(Product.class, "previousSalePriceInCents");
                    previousValueField.setAccessible(true);

                    ReflectionUtils.setField(previousValueField, updatedProduct, ReflectionUtils.getField(field, existingProduct));
                }
            }
        });

        if (productUpdates.get("salePriceInCents") != null) {
            updatedProduct.setPriceUpdateDate(new Timestamp(System.currentTimeMillis()));
        }

        updatedProduct.setCode(String.format("%13s", updatedProduct.getCode()));

        return updatedProduct;
    }
}
