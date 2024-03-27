package com.davifaustino.productregistrationsystem.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.davifaustino.productregistrationsystem.entities.EnumCategories;
import com.davifaustino.productregistrationsystem.entities.ProductType;
import com.davifaustino.productregistrationsystem.exceptions.RecordConflictException;
import com.davifaustino.productregistrationsystem.repositories.ProductTypeRepository;

@ExtendWith(MockitoExtension.class)
public class ProductTypeServiceTest {
    @Mock
    ProductTypeRepository productTypeRepository;

    @InjectMocks
    ProductTypeService productTypeService;

    ProductType productType;

    @BeforeEach
    void setup() {
        productType = new ProductType("Sabão em barra", EnumCategories.LIMPEZA_E_HIGIENE, null, (short) 2);
    }

    @Test
    @DisplayName("Must save a product type to the database successfully")
    void testSaveProductTypeCase1() {
        when(productTypeRepository.existsById(any())).thenReturn(false);
        when(productTypeRepository.save(any(ProductType.class))).thenReturn(productType);

        ProductType productTypeReturn = productTypeService.saveProductType(productType);

        assertTrue(productTypeReturn.equals(productType));
        verify(productTypeRepository).save(any(ProductType.class));
    }

    @Test
    @DisplayName("Must throw an error when trying to save a product type to the database")
    void testSaveProductTypeCase2() {
        when(productTypeRepository.existsById(any())).thenReturn(true);

        assertThrows(RecordConflictException.class, () -> productTypeService.saveProductType(productType));
        verifyNoMoreInteractions(productTypeRepository);
    }
}
