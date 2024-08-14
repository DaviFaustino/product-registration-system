package com.davifaustino.productregistrationsystem.api.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.davifaustino.productregistrationsystem.api.dtos.requests.ProductTypeRequest;
import com.davifaustino.productregistrationsystem.api.dtos.responses.ProductTypeResponse;
import com.davifaustino.productregistrationsystem.business.entities.EnumCategory;
import com.davifaustino.productregistrationsystem.business.entities.ProductType;
import com.davifaustino.productregistrationsystem.config.ModelMapperConfig;
import com.davifaustino.productregistrationsystem.config.ObjectMapperConfig;

@SpringBootTest(classes = ProductTypeMapper.class)
@Import({ModelMapperConfig.class, ObjectMapperConfig.class})
public class ProductTypeMapperTest {

    @Autowired
    ProductTypeMapper productTypeMapper;

    @Test
    void testToEntity() {
        ProductTypeRequest productTypeRequest = new ProductTypeRequest("1kg de Feijão", EnumCategory.FOOD_STAPLES_FOR_RESALE, (short) 2);
        
        ProductType mapperResponse = productTypeMapper.toEntity(productTypeRequest);

        assertEquals(productTypeRequest.getName(), mapperResponse.getName());
        assertEquals(productTypeRequest.getCategory(), mapperResponse.getCategory());
        assertEquals(productTypeRequest.getFullStockFactor(), mapperResponse.getFullStockFactor());
    }

    @Test
    void testToResponse() {
        ProductType productType = new ProductType("1kg de Feijão", EnumCategory.FOOD_STAPLES_FOR_RESALE,1000, (short) 2);

        ProductTypeResponse mapperResponse = productTypeMapper.toResponse(productType);

        assertEquals(productType.getName(), mapperResponse.getName());
        assertEquals(productType.getCategory(), mapperResponse.getCategory());
        assertEquals(productType.getAveragePriceInCents(), mapperResponse.getAveragePriceInCents());
        assertEquals(productType.getFullStockFactor(), mapperResponse.getFullStockFactor());
    }

    @Test
    void testToResponseList() {
        List<ProductType> productTypeList = new ArrayList<>();
        productTypeList.add(new ProductType("1kg de Feijão", EnumCategory.FOOD_STAPLES_FOR_RESALE,1000, (short) 2));
        productTypeList.add(new ProductType("1kg de Arroz", EnumCategory.FOOD_STAPLES_FOR_RESALE,750, (short) 2));

        List<ProductTypeResponse> mapperResponses = productTypeMapper.toResponseList(productTypeList);

        mapperResponses.forEach(response -> {
            int index = mapperResponses.indexOf(response);

            assertEquals(ProductTypeResponse.class, response.getClass());
            assertEquals(productTypeList.get(index).getName(), response.getName());
            assertEquals(productTypeList.get(index).getCategory(), response.getCategory());
            assertEquals(productTypeList.get(index).getAveragePriceInCents(), response.getAveragePriceInCents());
            assertEquals(productTypeList.get(index).getFullStockFactor(), response.getFullStockFactor());
        });
    }

    @Test
    void testToMap() {
        ProductTypeRequest productTypeRequest = new ProductTypeRequest("1kg de Feijão", EnumCategory.FOOD_STAPLES_FOR_RESALE, (short) 2);
        
        Map<String, Object> response = productTypeMapper.toMap(productTypeRequest);

        assertEquals(productTypeRequest.getName(), response.get("name"));
        assertEquals(productTypeRequest.getCategory().toString(), response.get("category").toString());
        assertEquals(productTypeRequest.getFullStockFactor(), response.get("fullStockFactor"));
    }
}
