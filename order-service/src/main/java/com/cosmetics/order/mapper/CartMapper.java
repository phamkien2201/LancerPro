package com.cosmetics.order.mapper;

import com.cosmetics.order.dto.request.CartRequest;
import com.cosmetics.order.dto.response.CartResponse;
import com.cosmetics.order.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartResponse toCartResponse(Cart cart);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cartItemIds", ignore = true)
    Cart toCart(CartRequest request);

    @Mapping(target = "id", ignore = true)
    void updateCart(@MappingTarget Cart cart, CartRequest request);
}
