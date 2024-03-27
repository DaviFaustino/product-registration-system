package com.davifaustino.productregistrationsystem.api.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.davifaustino.productregistrationsystem.api.dtos.ProductTypeDto;
import com.davifaustino.productregistrationsystem.entities.ProductType;

@Component
public class ProductTypeMapper {

    @Autowired
    private ModelMapper modelMapper;

    public ProductType toEntity(ProductTypeDto dto) {
        return modelMapper.map(dto, ProductType.class);
    }

    public ProductTypeDto toDto(ProductType entity) {
        return modelMapper.map(entity, ProductTypeDto.class);
    }
}
