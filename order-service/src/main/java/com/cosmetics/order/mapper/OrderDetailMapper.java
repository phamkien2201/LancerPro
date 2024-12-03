package com.cosmetics.order.mapper;

import com.cosmetics.order.dto.request.OrderDetailRequest;
import com.cosmetics.order.dto.request.OrderRequest;
import com.cosmetics.order.dto.response.OrderDetailResponse;
import com.cosmetics.order.entity.Order;
import com.cosmetics.order.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);

    OrderDetail toOrderDetail(OrderDetailRequest request);
    @Mapping(target = "id", ignore = true)
    void updateOrder(@MappingTarget OrderDetail orderDetail, OrderDetailRequest request);
}
