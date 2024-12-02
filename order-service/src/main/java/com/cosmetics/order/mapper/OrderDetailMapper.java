package com.cosmetics.order.mapper;

import com.cosmetics.order.dto.request.OrderDetailRequest;
import com.cosmetics.order.dto.response.OrderDetailResponse;
import com.cosmetics.order.entity.OrderDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);

    OrderDetail toOrderDetail(OrderDetailRequest request);
}
