package com.davifaustino.productregistrationsystem.business.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.davifaustino.productregistrationsystem.business.entities.Product;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("Must update a product successfully")
    void testUpdateByCodeOrName() {
        Product productUpdates = new Product("          003", "Biscoito", "Nikito", "", 2, 1, 2, 1, new Timestamp(1719802800000l), true);

        productRepository.updateByCode("          003", productUpdates);
        Product updatedProduct = productRepository.findById("          003").orElse(null);

        assertNotNull(updatedProduct);
        assertEquals(productUpdates.getName(), updatedProduct.getName());
        assertEquals(productUpdates.getPurchasePriceInCents(), updatedProduct.getPurchasePriceInCents());
        assertEquals(productUpdates.getSalePriceInCents(), updatedProduct.getSalePriceInCents());
        assertEquals(productUpdates.getPriceUpdateDate(), updatedProduct.getPriceUpdateDate());
    }

    @Test
    @DisplayName("Must find a product price list successfully")
    void testFindPricesByProductType() {
        List<Integer> repositoryReturn = productRepository.findPricesByProductTypeName("Biscoito");

        assertEquals(repositoryReturn, Arrays.asList(200, 150, 150, 200));
    }
}
