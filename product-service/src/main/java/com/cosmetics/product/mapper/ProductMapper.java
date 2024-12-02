package com.cosmetics.product.mapper;

import com.cosmetics.product.dto.request.ProductRequest;
import com.cosmetics.product.dto.response.ProductResponse;
import com.cosmetics.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductRequest request);
    ProductResponse toProductResponse(Product entity);

    @Mapping(target = "id", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductRequest request);
}
