package com.davifaustino.productregistrationsystem.api.mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.davifaustino.productregistrationsystem.api.dtos.requests.ProductTypeRequest;
import com.davifaustino.productregistrationsystem.api.dtos.responses.ProductTypeResponse;
import com.davifaustino.productregistrationsystem.business.entities.ProductType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProductTypeMapper {

    @Autowired
    private ObjectMapper objectMapper;

    public ProductType toEntity(ProductTypeRequest request) {
        return objectMapper.convertValue(request, ProductType.class);
    }

    public ProductTypeResponse toResponse(ProductType entity) {
        return objectMapper.convertValue(entity, ProductTypeResponse.class);
    }

    public List<ProductTypeResponse> toResponseList(List<ProductType> entityList) {
        return entityList.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Map<String, Object> toMap(ProductTypeRequest request) {
        return objectMapper.convertValue(request, new TypeReference<Map<String, Object>>() {});
    }
}
