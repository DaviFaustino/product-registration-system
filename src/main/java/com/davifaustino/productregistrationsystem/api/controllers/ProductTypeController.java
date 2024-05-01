package com.davifaustino.productregistrationsystem.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davifaustino.productregistrationsystem.api.dtos.ErrorResponseDto;
import com.davifaustino.productregistrationsystem.api.dtos.ProductTypeDto;
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
                    content = {@Content(schema = @Schema(implementation =  ProductTypeDto.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid request content",
                    content = {@Content(schema = @Schema(implementation =  ErrorResponseDto.class))}),
        @ApiResponse(responseCode = "409", description = "Product type already exists in the database",
                    content = {@Content(schema = @Schema(implementation =  ErrorResponseDto.class))})
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductTypeDto> saveProductType (@RequestBody @Valid ProductTypeDto dto) {
        ProductType response = productTypeService.saveProductType(productTypeMapper.toEntity(dto));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(productTypeMapper.toDto(response));
    }

    @Operation(summary = "Get a list of product types")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Response successfully received"),
        @ApiResponse(responseCode = "400", description = "Invalid request content")
    })
    @GetMapping
    public ResponseEntity<List<ProductType>> getProductTypes(@RequestParam(defaultValue = "") String searchTerm, @RequestParam(required = false) EnumCategory category) {
        List<ProductType> response = productTypeService.getProductTypes(searchTerm, Optional.ofNullable(category));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
