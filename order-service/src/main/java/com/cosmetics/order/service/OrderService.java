package com.cosmetics.order.service;

import com.cosmetics.order.dto.request.CartItem;
import com.cosmetics.order.dto.request.OrderRequest;
import com.cosmetics.order.dto.response.OrderListResponse;
import com.cosmetics.order.dto.response.OrderResponse;
import com.cosmetics.order.dto.response.UserResponse;
import com.cosmetics.order.entity.Order;
import com.cosmetics.order.entity.OrderDetail;
import com.cosmetics.order.entity.OrderStatus;
import com.cosmetics.order.exception.AppException;
import com.cosmetics.order.exception.ErrorCode;
import com.cosmetics.order.mapper.OrderDetailMapper;
import com.cosmetics.order.repository.OrderDetailRepository;
import com.cosmetics.order.repository.OrderRepository;
import com.cosmetics.order.mapper.OrderMapper;
import com.cosmetics.order.repository.httpclient.ProductClient;
import com.cosmetics.order.repository.httpclient.UserClient;
import com.cosmetics.order.dto.ApiResponse;
import com.cosmetics.order.dto.response.ProductResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderDetailMapper orderDetailMapper;
    ProductClient productClient;
    OrderDetailRepository orderDetailRepository;
    UserClient userClient;

    public OrderResponse createOrder(OrderRequest request) {

       ApiResponse<UserResponse> user = userClient.getUser(request.getUserId());
        if (user == null || user.getResult() == null) {
            throw new RuntimeException("User not found for ID: " + request.getUserId());
        }

       Order order = orderMapper.toOrder(request);
       order = orderRepository.save(order);
        //
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItem cartItem : request.getCartItemsId()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getId());

            String productId = cartItem.getProductId();
            Float quantity = cartItem.getQuantity();

            ApiResponse<ProductResponse> product = productClient.getProductById(productId);
            if (product == null || product.getResult() == null) {
                throw new RuntimeException("Product not found for ID: " + productId);
            };
            orderDetail.setProductId(productId);
            orderDetail.setQuantity(quantity);
            orderDetail.setPrice(product.getResult().getPrice());
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        return orderMapper.toOrderResponse(order);
    }

    public OrderResponse getOrder(String id) {
        return orderMapper.toOrderResponse(
                orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponse updateOrder(String orderId, OrderRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));

        orderMapper.updateOrder(order, request);
        order = orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void updateOrderStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
        order.setStatus(status);
        orderRepository.save(order);
    }

    private boolean isValidStatus(String status) {
        return OrderStatus.PENDING.equals(status) ||
                OrderStatus.PROCESSING.equals(status) ||
                OrderStatus.SHIPPED.equals(status) ||
                OrderStatus.DELIVERED.equals(status) ||
                OrderStatus.CANCELLED.equals(status);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public OrderListResponse getAllOrders(int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<Order> orderPage = orderRepository.findAll(pageRequest);

        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());

        return new OrderListResponse(orderResponses, orderPage.getTotalPages());
    }

    public List<OrderResponse> findByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

}
