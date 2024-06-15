package com.davifaustino.productregistrationsystem.api.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.davifaustino.productregistrationsystem.api.dtos.requests.ProductRequest;
import com.davifaustino.productregistrationsystem.api.dtos.responses.ProductResponse;
import com.davifaustino.productregistrationsystem.business.entities.Product;

@Component
public class ProductMapper {
    
    @Autowired
    private ModelMapper modelMapper;

    public Product toEntity(ProductRequest request) {
        return modelMapper.map(request, Product.class);
    }

    public ProductResponse toResponse(Product entity) {
        return modelMapper.map(entity, ProductResponse.class);
    }

    public List<ProductResponse> toResponseList(List<Product> entityList) {
        return entityList.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
