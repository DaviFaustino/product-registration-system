package com.davifaustino.productregistrationsystem.business.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.davifaustino.productregistrationsystem.business.entities.Product;
import com.davifaustino.productregistrationsystem.business.exceptions.RecordConflictException;
import com.davifaustino.productregistrationsystem.business.repositories.ProductRepository;
import com.davifaustino.productregistrationsystem.business.repositories.ProductTypeRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductTypeRepository productTypeRepository;

    @InjectMocks
    ProductService productService;

    Product product;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setProductTypeName("");
        product.setName("");
        product.setDescription("");
        product.setPurchasePriceInCents(1);
        product.setPreviousPurchasePriceInCents(1);
        product.setSalePriceInCents(1);
        product.setPreviousSalePriceInCents(1);
        product.setPriceUpdateDate(new Timestamp(1));
        product.setFullStock(true);
    }

    @Test
    @DisplayName("Must save a product to the database successfully")
    void testSaveProduct1() {
        product.setCode("1111111111111");

        when(productRepository.existsById(any())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productTypeRepository.existsById(any())).thenReturn(true);

        Product productReturn = productService.saveProduct(product);

        assertTrue(productReturn.equals(product));
        verify(productRepository, times(0)).findByCodeContaining(any());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Must create a new code for the product and save it to the database successfully")
    void testSaveProduct2() {
        ArrayList<Product> productList = new ArrayList<>();
        productList.add(new Product("          003", "", "", "", 1, 1, 1, 1, new Timestamp(1), true));

        when(productRepository.existsById(any())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productRepository.findByCodeContaining(any())).thenReturn(productList);
        when(productTypeRepository.existsById(any())).thenReturn(true);

        Product productReturn = productService.saveProduct(product);

        assertTrue(productReturn.equals(product));
        assertTrue(productReturn.getCode().equals("          004"));
        verify(productRepository, times(1)).findByCodeContaining(any());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Must throw an RecordConflictException when trying to save a product to the database")
    void testSaveProduct3() {
        product.setCode("1111111111111");

        when(productRepository.existsById(any())).thenReturn(true);

        assertThrows(RecordConflictException.class, () -> productService.saveProduct(product));
    }

    @Test
    @DisplayName("Must throw an DataIntegrityViolationException when trying to save a product to the database")
    void testSaveProduct4() {
        product.setCode("1111111111111");

        when(productRepository.existsById(any())).thenReturn(false);
        when(productTypeRepository.existsById(any())).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> productService.saveProduct(product));
    }
}
