package com.cosmetics.order.mapper;

import com.cosmetics.order.dto.request.OrderRequest;
import com.cosmetics.order.dto.response.OrderResponse;
import com.cosmetics.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "status", expression = "java(com.cosmetics.order.entity.OrderStatus.PENDING)")
    Order toOrder(OrderRequest request);

    @Mapping(target = "id", ignore = true)
    void updateOrder(@MappingTarget Order order, OrderRequest request);
}
