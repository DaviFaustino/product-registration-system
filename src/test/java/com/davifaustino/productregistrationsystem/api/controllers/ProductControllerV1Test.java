package com.davifaustino.productregistrationsystem.api.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.davifaustino.productregistrationsystem.api.dtos.requests.ProductRequest;
import com.davifaustino.productregistrationsystem.api.dtos.requests.ProductUpdateRequest;
import com.davifaustino.productregistrationsystem.api.dtos.responses.ProductResponse;
import com.davifaustino.productregistrationsystem.api.dtos.responses.ProductWithRecentPriceResponse;
import com.davifaustino.productregistrationsystem.api.mappers.ProductMapper;
import com.davifaustino.productregistrationsystem.business.entities.Product;
import com.davifaustino.productregistrationsystem.business.exceptions.InvalidSearchException;
import com.davifaustino.productregistrationsystem.business.exceptions.NonExistingRecordException;
import com.davifaustino.productregistrationsystem.business.exceptions.RecordConflictException;
import com.davifaustino.productregistrationsystem.business.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductControllerV1.class)
public class ProductControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    private Product product;
    private ProductRequest productRequest;
    private ProductResponse productResponse;
    private String requestAsJson;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ProductUpdateRequest productUpdateRequest;
    private ProductWithRecentPriceResponse recentPriceResponse;
    private String updateRequestAsJson;
    private Map<String, Object> productUpdates;

    @BeforeEach
    void setup() throws JsonProcessingException {
        product = new Product("7902635410298", "Margarina", "Delícia", "b", 1, 1, 1, 1, new Timestamp(1715966809874l), true);
        productRequest = new ProductRequest("7902635410298", "Margarina", "Delícia", "b",  1, 1, true);
        productResponse = new ProductResponse("7902635410298", "Margarina", "Delícia", "b", 1, 1, 1, 1, new Timestamp(1715966809874l), true);
        requestAsJson = objectMapper.writeValueAsString(productRequest);
        productUpdateRequest = new ProductUpdateRequest(null, null, null, null, 2, 3, null);
        recentPriceResponse = new ProductWithRecentPriceResponse("Delícia", 1, 1);
        updateRequestAsJson = objectMapper.writeValueAsString(productUpdateRequest);
        productUpdates = Map.of("purchasePriceInCents", 2, "salePriceInCents", 3);
    }

    @Test
    @DisplayName("Must create and return a Product successfully")
    void testSaveProductCase1() throws Exception {
        when(productMapper.toEntity(any())).thenReturn(product);
        when(productService.saveProduct(any(), any(Boolean.class))).thenReturn(product);
        when(productMapper.toResponse(any())).thenReturn(productResponse);

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code", is(product.getCode())))
                .andExpect(jsonPath("$.productTypeName", is(product.getProductTypeName())))
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andExpect(jsonPath("$.description", is(product.getDescription())))
                .andExpect(jsonPath("$.purchasePriceInCents", is(product.getPurchasePriceInCents())))
                .andExpect(jsonPath("$.salePriceInCents", is(product.getSalePriceInCents())))
                .andExpect(jsonPath("$.fullStock", is(product.getFullStock())));
        
        verify(productService).saveProduct(any(), any(Boolean.class));
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("Must throw a RecordConflictException")
    void testSaveProductCase2() throws Exception {
        when(productMapper.toEntity(any())).thenReturn(product);
        when(productService.saveProduct(any(), any(Boolean.class))).thenThrow(new RecordConflictException("The Product already exists in the database"));

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("The Product already exists in the database")))
                .andExpect(jsonPath("$.time", notNullValue()))
                .andExpect(jsonPath("$.path", is("/v1/products")))
                .andExpect(jsonPath("$.method", is("POST")));

        verify(productMapper, VerificationModeFactory.times(0)).toResponse(any());
    }

    @Test
    @DisplayName("Must throw a validation exception")
    void testSaveProductCase3() throws Exception {
        productRequest.setName(null);
        requestAsJson = objectMapper.writeValueAsString(productRequest);

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.time", notNullValue()))
                .andExpect(jsonPath("$.path", is("/v1/products")))
                .andExpect(jsonPath("$.method", is("POST")));
    }

    @Test
    @DisplayName("Must throw a NonExistingRecordException")
    void testSaveProductCase4() throws Exception {
        when(productMapper.toEntity(any())).thenReturn(product);
        when(productService.saveProduct(any(), any(Boolean.class))).thenThrow(new NonExistingRecordException(""));

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("")))
                .andExpect(jsonPath("$.time", notNullValue()))
                .andExpect(jsonPath("$.path", is("/v1/products")))
                .andExpect(jsonPath("$.method", is("POST")));

        verify(productMapper, VerificationModeFactory.times(0)).toResponse(any());
    }

    @Test
    @DisplayName("Must respond with a product list successfully")
    void testGetProducts1() throws Exception {
        List<Product> productList = new ArrayList<>();
        List<ProductResponse> productResponseList = new ArrayList<>();
        productList.add(product);
        productResponseList.add(productResponse);

        when(productService.getProducts(any(), any())).thenReturn(productList);
        when(productMapper.toResponseList(productList)).thenReturn(productResponseList);

        mockMvc.perform(get("/v1/products")
                        .param("searchTerm", "Arroz")
                        .param("productTypeName", "Arroz")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.startsWith("[")));
    }

    @Test
    @DisplayName("Must respond with an error message")
    void testGetProducts2() throws Exception {
        when(productService.getProducts(any(), any())).thenThrow(new InvalidSearchException("Exception"));

        mockMvc.perform(get("/v1/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("searchTerm", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Exception")))
                .andExpect(jsonPath("$.time", notNullValue()))
                .andExpect(jsonPath("$.path", is("/v1/products")))
                .andExpect(jsonPath("$.method", is("GET")));
    }

    @Test
    @DisplayName("Must respond with a recent price list successfully")
    void testGetProductsWithRecentPriceUpdate1() throws Exception {
        List<Product> productList = new ArrayList<>();
        List<ProductWithRecentPriceResponse> recentPriceList = new ArrayList<>();
        productList.add(product);
        recentPriceList.add(recentPriceResponse);

        when(productService.getProductsWithRecentPriceUpdate(1715000000000l)).thenReturn(productList);
        when(productMapper.toRecentPriceList(productList)).thenReturn(recentPriceList);

        mockMvc.perform(get("/v1/products/recent-price-updates")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("initialTime", "1715000000000"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Must respond with an error message and status code 400")
    void testGetProductsWithRecentPriceUpdate2() throws Exception {

        mockMvc.perform(get("/v1/products/recent-price-updates")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("initialTime", "false"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Must update the product successfully")
    void testUpdateProduct1() throws Exception {
        when(productMapper.toMap(productUpdateRequest)).thenReturn(productUpdates);
        when(productService.updateProduct(any(), any())).thenReturn(1);

        mockMvc.perform(patch("/v1/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("code", "          003")
                        .content(updateRequestAsJson))
                .andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("Must respond with an error message and status 404")
    void testUpdateProduct2() throws Exception {
        when(productMapper.toMap(productUpdateRequest)).thenReturn(productUpdates);
        when(productService.updateProduct(any(), any())).thenThrow(NonExistingRecordException.class);

        mockMvc.perform(patch("/v1/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("code", "          003")
                        .content(updateRequestAsJson))
                .andExpect(status().isNotFound());
    }
}
