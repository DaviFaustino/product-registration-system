package com.davifaustino.productregistrationsystem.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davifaustino.productregistrationsystem.api.dtos.requests.ProductTypeRequest;
import com.davifaustino.productregistrationsystem.api.dtos.responses.ErrorResponse;
import com.davifaustino.productregistrationsystem.api.dtos.responses.ProductTypeResponse;
import com.davifaustino.productregistrationsystem.api.mappers.ProductTypeMapper;
import com.davifaustino.productregistrationsystem.business.entities.EnumCategory;
import com.davifaustino.productregistrationsystem.business.entities.ProductType;
import com.davifaustino.productregistrationsystem.business.services.ProductTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(value = "/product-types", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "ProductTypes")
public class ProductTypeController {

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Operation(summary = "Create a new product type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product type saved successfully",
                    content = {@Content(schema = @Schema(implementation =  ProductTypeResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid request content",
                    content = {@Content(schema = @Schema(implementation =  ErrorResponse.class))}),
        @ApiResponse(responseCode = "409", description = "Product type already exists in the database",
                    content = {@Content(schema = @Schema(implementation =  ErrorResponse.class))})
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductTypeResponse> saveProductType (@RequestBody @Valid ProductTypeRequest request) {
        ProductType serviceResponse = productTypeService.saveProductType(productTypeMapper.toEntity(request));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(productTypeMapper.toResponse(serviceResponse));
    }

    @Operation(summary = "Get a list of product types")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Response successfully received"),
        @ApiResponse(responseCode = "400", description = "Invalid request content")
    })
    @GetMapping
    public ResponseEntity<List<ProductTypeResponse>> getProductTypes(@RequestParam(defaultValue = "") String searchTerm, @RequestParam(required = false) EnumCategory category) {
        List<ProductType> serviceResponse = productTypeService.getProductTypes(searchTerm, Optional.ofNullable(category));

        return ResponseEntity.status(HttpStatus.OK).body(productTypeMapper.toResponseList(serviceResponse));
    }

    @Operation(summary = "Get a list of product type names")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Response successfully received")
    })
    @GetMapping("/names")
    public ResponseEntity<List<String>> getProductTypeNames() {
        return ResponseEntity.status(HttpStatus.OK).body(productTypeService.getProductTypeNames());
    }

    @Operation(summary = "Update a product type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Response successfully accepted"),
        @ApiResponse(responseCode = "404", description = "Product type not found")
    })
    @PatchMapping("/{name}")
    public ResponseEntity<Integer> updateProductType(@PathVariable(value = "name") String name, @RequestBody ProductTypeRequest request) {
        Integer serviceResponse = productTypeService.updateProductType(name, productTypeMapper.toMap(request));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(serviceResponse);
    }
}
