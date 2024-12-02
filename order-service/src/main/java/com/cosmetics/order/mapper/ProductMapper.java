package com.cosmetics.order.mapper;

import com.cosmetics.order.dto.request.ProductRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductRequest toProductRequest(ProductRequest request);
}
