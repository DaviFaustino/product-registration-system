package com.davifaustino.productregistrationsystem.api.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.davifaustino.productregistrationsystem.api.dtos.ProductTypeDto;
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
        ProductTypeDto productTypeDto = new ProductTypeDto("1kg de Feijão", EnumCategory.ALIMENTOS_REVENDA,1000, (short) 2);
        
        ProductType response = productTypeMapper.toEntity(productTypeDto);

        assertEquals(ProductType.class, response.getClass());
        assertEquals(productTypeDto.getName(), response.getName());
        assertEquals(productTypeDto.getCategory(), response.getCategory());
        assertEquals(productTypeDto.getAveragePriceInCents(), response.getAveragePriceInCents());
        assertEquals(productTypeDto.getFullStockFactor(), response.getFullStockFactor());
    }

    @Test
    void testToDto() {
        ProductType productType = new ProductType("1kg de Feijão", EnumCategory.ALIMENTOS_REVENDA,1000, (short) 2);

        ProductTypeDto response = productTypeMapper.toDto(productType);

        assertEquals(ProductTypeDto.class, response.getClass());
        assertEquals(productType.getName(), response.getName());
        assertEquals(productType.getCategory(), response.getCategory());
        assertEquals(productType.getAveragePriceInCents(), response.getAveragePriceInCents());
        assertEquals(productType.getFullStockFactor(), response.getFullStockFactor());
    }

    @Test
    void testToDtoList() {
        List<ProductType> productTypeList = new ArrayList<>();
        productTypeList.add(new ProductType("1kg de Feijão", EnumCategory.ALIMENTOS_REVENDA,1000, (short) 2));
        productTypeList.add(new ProductType("1kg de Arroz", EnumCategory.ALIMENTOS_REVENDA,750, (short) 2));

        List<ProductTypeDto> response = productTypeMapper.toDtoList(productTypeList);

        response.forEach(dto -> {
            int index = response.indexOf(dto);

            assertEquals(ProductTypeDto.class, dto.getClass());
            assertEquals(productTypeList.get(index).getName(), dto.getName());
            assertEquals(productTypeList.get(index).getCategory(), dto.getCategory());
            assertEquals(productTypeList.get(index).getAveragePriceInCents(), dto.getAveragePriceInCents());
            assertEquals(productTypeList.get(index).getFullStockFactor(), dto.getFullStockFactor());
        });
    }

    @Test
    void testToMap() {
        ProductTypeDto productTypeDto = new ProductTypeDto("1kg de Feijão", EnumCategory.ALIMENTOS_REVENDA,1000, (short) 2);
        
        Map<String, Object> response = productTypeMapper.toMap(productTypeDto);

        assertEquals(productTypeDto.getName(), response.get("name"));
        assertEquals(productTypeDto.getCategory().toString(), response.get("category").toString());
        assertEquals(productTypeDto.getAveragePriceInCents(), response.get("averagePriceInCents"));
        assertEquals(productTypeDto.getFullStockFactor(), response.get("fullStockFactor"));
    }
}
