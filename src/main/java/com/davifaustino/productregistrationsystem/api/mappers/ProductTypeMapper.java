package com.davifaustino.productregistrationsystem.api.mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.davifaustino.productregistrationsystem.api.dtos.ProductTypeDto;
import com.davifaustino.productregistrationsystem.business.entities.ProductType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProductTypeMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    public ProductType toEntity(ProductTypeDto dto) {
        return modelMapper.map(dto, ProductType.class);
    }

    public ProductTypeDto toDto(ProductType entity) {
        return modelMapper.map(entity, ProductTypeDto.class);
    }

    public List<ProductTypeDto> toDtoList(List<ProductType> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Map<String, Object> toMap(ProductTypeDto dto) {
        return objectMapper.convertValue(dto, new TypeReference<Map<String, Object>>() {});
    }
}
