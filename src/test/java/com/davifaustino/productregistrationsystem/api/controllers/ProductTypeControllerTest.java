package com.davifaustino.productregistrationsystem.api.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.davifaustino.productregistrationsystem.api.dtos.ProductTypeDto;
import com.davifaustino.productregistrationsystem.api.mappers.ProductTypeMapper;
import com.davifaustino.productregistrationsystem.entities.EnumCategory;
import com.davifaustino.productregistrationsystem.entities.ProductType;
import com.davifaustino.productregistrationsystem.exceptions.RecordConflictException;
import com.davifaustino.productregistrationsystem.services.ProductTypeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductTypeController.class)
public class ProductTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductTypeService productTypeService;

    @MockBean
    private ProductTypeMapper productTypeMapper;

    private ProductType productType;
    private ProductTypeDto productTypeDto;
    private String dtoRequestAsJson;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() throws JsonProcessingException {
        productType = new ProductType("1kg de Arroz", EnumCategory.ALIMENTOS_REVENDA, 750, (short) 1);
        productTypeDto = new ProductTypeDto("1kg de Arroz", EnumCategory.ALIMENTOS_REVENDA, 750, (short) 1);
        dtoRequestAsJson = objectMapper.writeValueAsString(productTypeDto);
    }

    @Test
    @DisplayName("Must create and return a ProductType successfully")
    void testSaveProductTypeCase1() throws Exception {
        when(productTypeMapper.toEntity(any())).thenReturn(productType);
        when(productTypeService.saveProductType(any())).thenReturn(productType);
        when(productTypeMapper.toDto(any())).thenReturn(productTypeDto);

        mockMvc.perform(post("/product-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(dtoRequestAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(productType.getName())))
                .andExpect(jsonPath("$.category", is(productType.getCategory().toString())))
                .andExpect(jsonPath("$.averagePriceInCents", is(productType.getAveragePriceInCents())))
                .andExpect(jsonPath("$.fullStockFactor", is((int) productType.getFullStockFactor())));
        
        verify(productTypeService).saveProductType(any());
        verifyNoMoreInteractions(productTypeService);
    }

    @Test
    @DisplayName("Must throw a RecordConflictException")
    void testSaveProductTypeCase2() throws Exception {
        when(productTypeMapper.toEntity(any())).thenReturn(productType);
        when(productTypeService.saveProductType(any())).thenThrow(new RecordConflictException("The Product Type already exists in the database"));

        mockMvc.perform(post("/product-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoRequestAsJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("The Product Type already exists in the database")))
                .andExpect(jsonPath("$.time", notNullValue()))
                .andExpect(jsonPath("$.path", is("/product-types")))
                .andExpect(jsonPath("$.method", is("POST")));

        verify(productTypeMapper, VerificationModeFactory.times(0)).toDto(any());
    }

    @Test
    @DisplayName("Must throw a validation exception")
    void testSaveProductTypeCase3() throws Exception {
        productTypeDto.setName(null);
        dtoRequestAsJson = objectMapper.writeValueAsString(productTypeDto);

        mockMvc.perform(post("/product-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoRequestAsJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.time", notNullValue()))
                .andExpect(jsonPath("$.path", is("/product-types")))
                .andExpect(jsonPath("$.method", is("POST")));
    }

    @Test
    @DisplayName("Must return a list of product types sucessfully")
    void testGetProductTypesCase1() throws Exception {

        mockMvc.perform(get("/product-types")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("searchTerm", "d")
                        .param("category", "PANIFICAÇÃO"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.startsWith("[")));
    }

    @Test
    @DisplayName("Must throw a MethodArgumentTypeMismatchException")
    void testGetProductTypesCase2() throws Exception {

        mockMvc.perform(get("/product-types")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("category", "PANIFICA"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.time", notNullValue()))
                .andExpect(jsonPath("$.path", is("/product-types")))
                .andExpect(jsonPath("$.method", is("GET")));

        verify(productTypeService, times(0)).getProductTypes(any(), any());
    }
}
