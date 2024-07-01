package com.davifaustino.productregistrationsystem.business.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.davifaustino.productregistrationsystem.business.entities.Product;
import com.davifaustino.productregistrationsystem.business.exceptions.InvalidSearchException;
import com.davifaustino.productregistrationsystem.business.exceptions.NonExistingRecordException;
import com.davifaustino.productregistrationsystem.business.exceptions.RecordConflictException;
import com.davifaustino.productregistrationsystem.business.repositories.ProductRepository;
import com.davifaustino.productregistrationsystem.business.repositories.ProductTypeRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductTypeRepository productTypeRepository;

    @Mock
    ProductTypeService productTypeService;

    @InjectMocks
    ProductService productService;

    Product product;
    Map<String, Object> productUpdates;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setProductTypeName("");
        product.setName("");
        product.setDescription("");
        product.setPurchasePriceInCents(100);
        product.setSalePriceInCents(100);
        product.setPreviousPurchasePriceInCents(null);
        product.setPreviousSalePriceInCents(null);
        product.setPriceUpdateDate(new Timestamp(1));
        product.setFullStock(true);

        productUpdates = new HashMap<>();
        productUpdates.put("code", null);
        productUpdates.put("productTypeName", null);
        productUpdates.put("name", "Snack");
        productUpdates.put("description", null);
        productUpdates.put("purchasePriceInCents", 200);
        productUpdates.put("salePriceInCents", 300);
        productUpdates.put("fullStock", null);
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
    @DisplayName("Must throw an DataIntegrityViolationException when trying to save a product to the database because the product type doesn't exist")
    void testSaveProduct4() {
        product.setCode("1111111111111");

        when(productRepository.existsById(any())).thenReturn(false);
        when(productTypeRepository.existsById(any())).thenReturn(false);

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> productService.saveProduct(product));
        assertTrue(exception.getMessage().equals("The product type entered doesn't exist in the database"));
    }

    @Test
    @DisplayName("Must throw an DataIntegrityViolationException when trying to save a product to the database because the name isn't unique")
    void testSaveProduct5() {
        product.setCode("1111111111118");

        when(productRepository.existsById(any())).thenReturn(false);
        when(productTypeRepository.existsById(any())).thenReturn(true);
        when(productRepository.existsByName(any())).thenReturn(true);

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> productService.saveProduct(product));
        assertTrue(exception.getMessage().equals("The product name entered already exists in the database"));
    }

    @Test
    @DisplayName("Must get a list of products by code successfully")
    void testGetProducts1() {
        when(productRepository.findByCode(any())).thenReturn(Optional.of(product));
        
        List<Product> listReturn = productService.getProducts("1113", Optional.ofNullable(null));

        assertTrue(listReturn.size() != 0);
        verify(productRepository, times(1)).findByCode(any());
    }

    @Test
    @DisplayName("Must get a list of products by code successfully")
    void testGetProducts2() {
        when(productRepository.findByCode(any())).thenReturn(Optional.of(product));
        
        List<Product> listReturn = productService.getProducts("1113", Optional.of("Arroz"));

        assertTrue(listReturn.size() != 0);
        verify(productRepository, times(1)).findByCode(any());
    }

    @Test
    @DisplayName("Must throw an InvalidSearchException")
    void testGetProducts3() {
        InvalidSearchException exception = assertThrows(InvalidSearchException.class, () -> productService.getProducts("12", Optional.ofNullable(null)));

        assertTrue(exception.getMessage().equals("Code size out of range"));
    }

    @Test
    @DisplayName("Must get a list of products by name and product type name successfully")
    void testGetProducts4() {
        when(productRepository.findByNameIgnoreCaseContainingAndProductTypeName(any(), any())).thenReturn(Arrays.asList(product));

        List<Product> listReturn = productService.getProducts("Arroz", Optional.of("Arroz"));

        assertTrue(listReturn.size() != 0);
        verify(productRepository, times(1)).findByNameIgnoreCaseContainingAndProductTypeName(any(), any());
    }

    @Test
    @DisplayName("Must get a list of products by name successfully")
    void testGetProducts5() {
        when(productRepository.findByNameIgnoreCaseContaining(any())).thenReturn(Arrays.asList(product));

        List<Product> listReturn = productService.getProducts("Arroz", Optional.ofNullable(null));

        assertTrue(listReturn.size() != 0);
        verify(productRepository, times(1)).findByNameIgnoreCaseContaining(any());
    }

    @Test
    @DisplayName("Must throw an InvalidSearchException")
    void testGetProducts6() {
        InvalidSearchException exception = assertThrows(InvalidSearchException.class, () -> productService.getProducts("   ", Optional.ofNullable(null)));

        assertTrue(exception.getMessage().equals("No search parameters provided"));
    }

    @Test
    @DisplayName("Must update the product successfully")
    void testUpdateProduct1() {
        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        assertDoesNotThrow(() -> productService.updateProduct("", productUpdates));
    }

    @Test
    @DisplayName("Must throw an exception when trying to update the product")
    void testUpdateProduct2() {
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(NonExistingRecordException.class, () -> productService.updateProduct("", productUpdates));
    }

    @Test
    @DisplayName("Must return a product with the updates successfully")
    void testComposeUpdatedProduct() {
        product.setCode("          003");

        Product updatedProduct = productService.composeUpdatedProduct(productUpdates, product);

        assertEquals(updatedProduct.getCode(), "          003");
        assertEquals(updatedProduct.getProductTypeName(), "");
        assertEquals(updatedProduct.getName(), "Snack");
        assertEquals(updatedProduct.getDescription(), "");
        assertEquals(updatedProduct.getPurchasePriceInCents(), 200);
        assertEquals(updatedProduct.getSalePriceInCents(), 300);
        assertEquals(updatedProduct.getPreviousPurchasePriceInCents(), 100);
        assertEquals(updatedProduct.getPreviousSalePriceInCents(), 100);
        assertNotEquals(updatedProduct.getPriceUpdateDate(), new Timestamp(1));
        assertEquals(updatedProduct.getFullStock(), true);
    }
}
