package com.cosmetics.order.service;

import com.cosmetics.order.dto.request.OrderDetailRequest;
import com.cosmetics.order.dto.request.OrderRequest;
import com.cosmetics.order.dto.request.ProductRequest;
import com.cosmetics.order.dto.response.OrderDetailResponse;
import com.cosmetics.order.dto.response.OrderResponse;
import com.cosmetics.order.entity.Order;
import com.cosmetics.order.entity.OrderDetail;
import com.cosmetics.order.exception.AppException;
import com.cosmetics.order.exception.ErrorCode;
import com.cosmetics.order.mapper.OrderDetailMapper;
import com.cosmetics.order.repository.OrderDetailRepository;
import com.cosmetics.order.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailService {

    OrderDetailRepository orderDetailRepository;
    OrderRepository orderRepository;
    OrderDetailMapper orderDetailMapper;

    public OrderDetailResponse getOrderDetail(String id) {
        return orderDetailMapper.toOrderDetailResponse(
                orderDetailRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED)));
    }


    public OrderDetailResponse createOrderDetail(OrderDetailRequest request) {
        OrderDetail orderDetail = orderDetailMapper.toOrderDetail(request);
        orderDetail = orderDetailRepository.save(orderDetail);
        return orderDetailMapper.toOrderDetailResponse(orderDetail);
    }

    public OrderDetailResponse updateOrderDetail(String orderDetailId, OrderDetailRequest request) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));

        orderDetailMapper.updateOrder(orderDetail, request);
        orderDetail = orderDetailRepository.save(orderDetail);
        return orderDetailMapper.toOrderDetailResponse(orderDetail);
    }

    public void deleteOrderDetail(String id) {
        orderDetailRepository.deleteById(id);
    }

    public List<OrderDetailResponse> findByOrderId(String orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
