package com.davifaustino.productregistrationsystem.api.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.davifaustino.productregistrationsystem.api.dtos.ProductDto;
import com.davifaustino.productregistrationsystem.api.mappers.ProductMapper;
import com.davifaustino.productregistrationsystem.business.entities.Product;
import com.davifaustino.productregistrationsystem.business.exceptions.RecordConflictException;
import com.davifaustino.productregistrationsystem.business.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    private Product product;
    private ProductDto productDto;
    private String dtoRequestAsJson;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() throws JsonProcessingException {
        product = new Product("7902635410298", "Margarina", "Delícia", "b", 1, 1, 1, 1, new Timestamp(1715966809874l), true);
        productDto = new ProductDto("7902635410298", "Margarina", "Delícia", "b", 1, 1, 1, 1, new Timestamp(1715966809874l), true);
        dtoRequestAsJson = objectMapper.writeValueAsString(productDto);
    }

    @Test
    @DisplayName("Must create and return a Product successfully")
    void testSaveProductCase1() throws Exception {
        when(productMapper.toEntity(any())).thenReturn(product);
        when(productService.saveProduct(any())).thenReturn(product);
        when(productMapper.toDto(any())).thenReturn(productDto);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(dtoRequestAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code", is(product.getCode())))
                .andExpect(jsonPath("$.productTypeName", is(product.getProductTypeName())))
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andExpect(jsonPath("$.description", is(product.getDescription())))
                .andExpect(jsonPath("$.purchasePriceInCents", is(product.getPurchasePriceInCents())))
                .andExpect(jsonPath("$.previousPurchasePriceInCents", is(product.getPreviousPurchasePriceInCents())))
                .andExpect(jsonPath("$.salePriceInCents", is(product.getSalePriceInCents())))
                .andExpect(jsonPath("$.previousSalePriceInCents", is(product.getPreviousSalePriceInCents())))
                .andExpect(jsonPath("$.priceUpdateDate", is(LocalDateTime.ofInstant(Instant.ofEpochMilli(product.getPriceUpdateDate().getTime()), ZoneOffset.UTC)
                                                                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))))
                .andExpect(jsonPath("$.fullStock", is(product.getFullStock())));
        
        verify(productService).saveProduct(any());
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("Must throw a RecordConflictException")
    void testSaveProductCase2() throws Exception {
        when(productMapper.toEntity(any())).thenReturn(product);
        when(productService.saveProduct(any())).thenThrow(new RecordConflictException("The Product already exists in the database"));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoRequestAsJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("The Product already exists in the database")))
                .andExpect(jsonPath("$.time", notNullValue()))
                .andExpect(jsonPath("$.path", is("/products")))
                .andExpect(jsonPath("$.method", is("POST")));

        verify(productMapper, VerificationModeFactory.times(0)).toDto(any());
    }

    @Test
    @DisplayName("Must throw a validation exception")
    void testSaveProductCase3() throws Exception {
        productDto.setName(null);
        dtoRequestAsJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoRequestAsJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.time", notNullValue()))
                .andExpect(jsonPath("$.path", is("/products")))
                .andExpect(jsonPath("$.method", is("POST")));
    }

    @Test
    @DisplayName("Must throw a DataIntegrityViolationException")
    void testSaveProductCase4() throws Exception {
        when(productMapper.toEntity(any())).thenReturn(product);
        when(productService.saveProduct(any())).thenThrow(new DataIntegrityViolationException(""));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoRequestAsJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.time", notNullValue()))
                .andExpect(jsonPath("$.path", is("/products")))
                .andExpect(jsonPath("$.method", is("POST")));

        verify(productMapper, VerificationModeFactory.times(0)).toDto(any());
    }
}
