package com.davifaustino.productregistrationsystem.api.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.davifaustino.productregistrationsystem.api.dtos.requests.ProductTypeRequest;
import com.davifaustino.productregistrationsystem.api.dtos.responses.ProductTypeResponse;
import com.davifaustino.productregistrationsystem.api.mappers.ProductTypeMapper;
import com.davifaustino.productregistrationsystem.business.entities.EnumCategory;
import com.davifaustino.productregistrationsystem.business.entities.ProductType;
import com.davifaustino.productregistrationsystem.business.exceptions.NonExistingRecordException;
import com.davifaustino.productregistrationsystem.business.exceptions.RecordConflictException;
import com.davifaustino.productregistrationsystem.business.services.ProductTypeService;
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
    private ProductTypeRequest productTypeRequest;
    private ProductTypeResponse productTypeResponse;
    private String requestAsJson;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, Object> productTypeUpdates;

    @BeforeEach
    void setup() throws JsonProcessingException {
        productType = new ProductType("1kg de Arroz", EnumCategory.FOOD_STAPLES_FOR_RESALE, 0, (short) 1);
        productTypeRequest = new ProductTypeRequest("1kg de Arroz", EnumCategory.FOOD_STAPLES_FOR_RESALE, (short) 1);
        productTypeResponse = new ProductTypeResponse("1kg de Arroz", EnumCategory.FOOD_STAPLES_FOR_RESALE, 0, (short) 1);
        requestAsJson = objectMapper.writeValueAsString(productTypeRequest);
        productTypeUpdates = Map.of("name", "Sabão em pó", "fullStockFactor", (short) 1);
    }

    @Test
    @DisplayName("Must create and return a ProductType successfully")
    void testSaveProductTypeCase1() throws Exception {
        when(productTypeMapper.toEntity(any())).thenReturn(productType);
        when(productTypeService.saveProductType(any())).thenReturn(productType);
        when(productTypeMapper.toResponse(any())).thenReturn(productTypeResponse);

        mockMvc.perform(post("/product-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestAsJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(productType.getName())))
                .andExpect(jsonPath("$.category", is(productType.getCategory().toString())))
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
                        .content(requestAsJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("The Product Type already exists in the database")))
                .andExpect(jsonPath("$.time", notNullValue()))
                .andExpect(jsonPath("$.path", is("/product-types")))
                .andExpect(jsonPath("$.method", is("POST")));

        verify(productTypeMapper, VerificationModeFactory.times(0)).toResponse(any());
    }

    @Test
    @DisplayName("Must throw a validation exception")
    void testSaveProductTypeCase3() throws Exception {
        productTypeRequest.setName(null);
        requestAsJson = objectMapper.writeValueAsString(productTypeRequest);

        mockMvc.perform(post("/product-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson))
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
                        .param("category", "BAKING"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.startsWith("[")));
    }

    @Test
    @DisplayName("Must throw a MethodArgumentTypeMismatchException")
    void testGetProductTypesCase2() throws Exception {

        mockMvc.perform(get("/product-types")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("category", "BAKIN"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.time", notNullValue()))
                .andExpect(jsonPath("$.path", is("/product-types")))
                .andExpect(jsonPath("$.method", is("GET")));

        verify(productTypeService, times(0)).getProductTypes(any(), any());
    }

    @Test
    @DisplayName("Must update the product type successfully")
    void testUpdateProductType1() throws Exception {
        when(productTypeMapper.toMap(productTypeRequest)).thenReturn(productTypeUpdates);
        when(productTypeService.updateProductType(any(), any())).thenReturn(1);

        mockMvc.perform(patch("/product-types/{name}", "product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson))
                .andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("Must respond with an error message and status 404")
    void testUpdateProductType2() throws Exception {
        when(productTypeMapper.toMap(productTypeRequest)).thenReturn(productTypeUpdates);
        when(productTypeService.updateProductType(any(), any())).thenThrow(NonExistingRecordException.class);

        mockMvc.perform(patch("/product-types/{name}", "product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Must respond with an error message and status 404")
    void testUpdateProductType3() throws Exception {
        when(productTypeMapper.toMap(productTypeRequest)).thenReturn(productTypeUpdates);
        when(productTypeService.updateProductType(any(), any())).thenThrow(NonExistingRecordException.class);

        mockMvc.perform(patch("/product-types/{name}", "product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }
}
