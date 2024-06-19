package com.davifaustino.productregistrationsystem.api.mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.davifaustino.productregistrationsystem.api.dtos.requests.ProductTypeRequest;
import com.davifaustino.productregistrationsystem.api.dtos.responses.ProductTypeResponse;
import com.davifaustino.productregistrationsystem.business.entities.ProductType;

@Component
public class ProductTypeMapper {

    @Autowired
    private ModelMapper modelMapper;

    public ProductType toEntity(ProductTypeRequest request) {
        return modelMapper.map(request, ProductType.class);
    }

    public ProductTypeResponse toResponse(ProductType entity) {
        return modelMapper.map(entity, ProductTypeResponse.class);
    }

    public List<ProductTypeResponse> toResponseList(List<ProductType> entityList) {
        return entityList.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Map<String, Object> toMap(ProductTypeRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", request.getName());
        map.put("category", request.getCategory());
        map.put("fullStockFactor", request.getFullStockFactor());

        return map;
    }
}
