package com.davifaustino.productregistrationsystem.business.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.davifaustino.productregistrationsystem.business.entities.EnumCategory;
import com.davifaustino.productregistrationsystem.business.entities.ProductType;

@DataJpaTest
public class ProductTypeRepositoryTest {

    @Autowired
    ProductTypeRepository productTypeRepository;
    
    @Test
    @DisplayName("Must get a list of product type names in alphabetical order")
    void testFindAllProductTypeNames() {
        List<String> names = productTypeRepository.findAllProductTypeNames();

        assertEquals(Arrays.asList("Biscoito", "Danone", "Margarina", "Papel higiênico", "Pipoca", "Pão doce",
                                    "Reforçador", "Refri de 1L", "Vassoura", "Óleo"), names);
    }

    @Test
    @DisplayName("Must update a product type successfully")
    void testUpdateByName() {
        ProductType productTypeUpdates = new ProductType("Pão adoçado", EnumCategory.PANIFICAÇÃO, 75, (short) 2);

        productTypeRepository.updateByName("Pão doce", productTypeUpdates);
        ProductType updatedProductType = productTypeRepository.findById("Pão adoçado").orElse(null);

        assertNotNull(updatedProductType);
        assertEquals((productTypeUpdates.getName()), updatedProductType.getName());
        assertEquals(productTypeUpdates.getCategory(), updatedProductType.getCategory());
        assertEquals(productTypeUpdates.getFullStockFactor(), updatedProductType.getFullStockFactor());
    }
}
