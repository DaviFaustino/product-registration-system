package com.davifaustino.productregistrationsystem.api.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.davifaustino.productregistrationsystem.api.dtos.ProductDto;
import com.davifaustino.productregistrationsystem.business.entities.Product;

@Component
public class ProductMapper {
    
    @Autowired
    private ModelMapper modelMapper;

    public Product toEntity(ProductDto dto) {
        return modelMapper.map(dto, Product.class);
    }

    public ProductDto toDto(Product entity) {
        return modelMapper.map(entity, ProductDto.class);
    }
}
