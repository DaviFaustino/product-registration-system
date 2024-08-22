package com.davifaustino.productregistrationsystem.api.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.davifaustino.productregistrationsystem.api.dtos.requests.ProductRequest;
import com.davifaustino.productregistrationsystem.api.dtos.requests.ProductUpdateRequest;
import com.davifaustino.productregistrationsystem.api.dtos.responses.ErrorResponse;
import com.davifaustino.productregistrationsystem.api.dtos.responses.ProductResponse;
import com.davifaustino.productregistrationsystem.api.dtos.responses.ProductWithRecentPriceResponse;
import com.davifaustino.productregistrationsystem.api.mappers.ProductMapper;
import com.davifaustino.productregistrationsystem.business.entities.Product;
import com.davifaustino.productregistrationsystem.business.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/v1/products", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Products")
public class ProductControllerV1 {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product saved successfully",
                    content = {@Content(schema = @Schema(implementation =  ProductResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid request content",
                    content = {@Content(schema = @Schema(implementation =  ErrorResponse.class))}),
        @ApiResponse(responseCode = "409", description = "Product already exists in the database",
                    content = {@Content(schema = @Schema(implementation =  ErrorResponse.class))})
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> saveProduct (@RequestParam(defaultValue = "false") boolean isPriceOld, @RequestBody @Valid ProductRequest request) {
        Product serviceResponse = productService.saveProduct(productMapper.toEntity(request), isPriceOld);

        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toResponse(serviceResponse));
    }

    
    @Operation(summary = "Get a list of products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products received successfully",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation =  ProductResponse.class)))}),
        @ApiResponse(responseCode = "400", description = "Invalid request content",
                    content = {@Content(schema = @Schema(implementation =  ErrorResponse.class))})
    })
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(@RequestParam(defaultValue = "") String searchTerm, @RequestParam(required = false) String productTypeName) {
        List<Product> serviceResponse = productService.getProducts(searchTerm, Optional.ofNullable(productTypeName));

        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toResponseList(serviceResponse));
    }

    
    @Operation(summary = "Get a list of products with recent price update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recent prices successfully received",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation =  ProductWithRecentPriceResponse.class)))}),
        @ApiResponse(responseCode = "400", description = "Invalid request parameter",
                    content = {@Content(schema = @Schema(implementation =  ErrorResponse.class))})
    })
    @GetMapping(value = "/recent-price-updates")
    public ResponseEntity<List<ProductWithRecentPriceResponse>> getProductsWithRecentPriceUpdate(@RequestParam Long initialTime) {
        List<Product> serviceResponse = productService.getProductsWithRecentPriceUpdate(initialTime);

        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toRecentPriceList(serviceResponse));
    }


    @Operation(summary = "Update a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Product successfully updated",
                    content = {@Content(schema = @Schema(implementation =  Integer.class))}),
        @ApiResponse(responseCode = "400", description = "No update data provided",
                    content = {@Content(schema = @Schema(implementation =  ErrorResponse.class))}),
        @ApiResponse(responseCode = "404", description = "Product not found",
                    content = {@Content(schema = @Schema(implementation =  ErrorResponse.class))})
    })
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> updateProduct(@RequestParam(required = true) String code, @RequestBody @Valid ProductUpdateRequest request) {
        Integer serviceResponse = productService.updateProduct(code, productMapper.toMap(request));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(serviceResponse);
    }


    @Operation(summary = "Delete a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product successfully deleted",
                    content = {@Content(schema = @Schema(implementation =  Void.class))}),
        @ApiResponse(responseCode = "404", description = "Product not found",
                    content = {@Content(schema = @Schema(implementation =  ErrorResponse.class))})
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(@RequestParam(required = true) String code) {
        productService.deleteProduct(code);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
