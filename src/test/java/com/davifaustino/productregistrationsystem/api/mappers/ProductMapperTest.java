package com.davifaustino.productregistrationsystem.api.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.davifaustino.productregistrationsystem.api.dtos.ProductDto;
import com.davifaustino.productregistrationsystem.business.entities.Product;
import com.davifaustino.productregistrationsystem.config.ModelMapperConfig;

@SpringBootTest(classes = {ProductMapper.class, ModelMapperConfig.class})
public class ProductMapperTest {

    @Autowired
    ProductMapper productMapper;

    @Test
    void testToEntity() {
        ProductDto productDto = new ProductDto("1111111111111", "a", "a", "a", 1, 1, 1, 1, new Timestamp(1), true);

        Product response = productMapper.toEntity(productDto);

        assertEquals(Product.class, response.getClass());
        assertEquals(productDto.getCode(), response.getCode());
        assertEquals(productDto.getProductTypeName(), response.getProductTypeName());
        assertEquals(productDto.getName(), response.getName());
        assertEquals(productDto.getDescription(), response.getDescription());
        assertEquals(productDto.getPurchasePriceInCents(), response.getPurchasePriceInCents());
        assertEquals(productDto.getPreviousPurchasePriceInCents(), response.getPreviousPurchasePriceInCents());
        assertEquals(productDto.getSalePriceInCents(), response.getPreviousSalePriceInCents());
        assertEquals(productDto.getPreviousSalePriceInCents(), response.getPreviousSalePriceInCents());
        assertEquals(productDto.getPriceUpdateDate(), response.getPriceUpdateDate());
        assertEquals(productDto.getFullStock(), response.getFullStock());
    }
    
    @Test
    void testToDto() {
        Product product = new Product("1111111111111", "a", "a", "a", 1, 1, 1, 1, new Timestamp(1), true);

        ProductDto response = productMapper.toDto(product);

        assertEquals(ProductDto.class, response.getClass());
        assertEquals(product.getCode(), response.getCode());
        assertEquals(product.getProductTypeName(), response.getProductTypeName());
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getDescription(), response.getDescription());
        assertEquals(product.getPurchasePriceInCents(), response.getPurchasePriceInCents());
        assertEquals(product.getPreviousPurchasePriceInCents(), response.getPreviousPurchasePriceInCents());
        assertEquals(product.getSalePriceInCents(), response.getPreviousSalePriceInCents());
        assertEquals(product.getPreviousSalePriceInCents(), response.getPreviousSalePriceInCents());
        assertEquals(product.getPriceUpdateDate(), response.getPriceUpdateDate());
        assertEquals(product.getFullStock(), response.getFullStock());
    }
}
