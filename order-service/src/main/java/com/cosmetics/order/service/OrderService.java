package com.cosmetics.order.service;

import com.cosmetics.order.configuration.VNPAYConfig;
import com.cosmetics.order.dto.request.CartItemRequest;
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
    DistanceService distanceService;
    private static final String DEFAULT_ADDRESS = "106.70000,10.77689";

    public OrderResponse createOrder(OrderRequest request) {

       ApiResponse<UserResponse> user = userClient.getUser(request.getUserId());
        if (user == null || user.getResult() == null) {
            throw new RuntimeException("User not found for ID: " + request.getUserId());
        }
        String warehouseAddress = "Hiệp Thành, 71500, Quận 12, Thành phố Hồ Chí Minh, Việt Nam";
        float shippingFee = calculateShippingFee(request.getAddress(), request.getTotalMoney());

       Order order = orderMapper.toOrder(request);
        order.setFee(shippingFee);
        order = orderRepository.save(order);
        //
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemRequest cartItemRequest : request.getCartItemsIdRequest()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getId());

            String productId = cartItemRequest.getProductId();
            Float requestedQuantity = cartItemRequest.getQuantity();

            ApiResponse<ProductResponse> productResponse = productClient.getProductById(productId);
            if (productResponse == null || productResponse.getResult() == null) {
                throw new RuntimeException("Product not found for ID: " + productId);
            };
            ProductResponse product = productResponse.getResult();
            Float currentQuantity = product.getQuantity();

            // Kiểm tra số lượng có đủ hay không
            if (currentQuantity < requestedQuantity) {
                throw new RuntimeException("Insufficient quantity for product ID: " + productId);
            }

            Float newQuantity = currentQuantity - requestedQuantity;
            productClient.updateProductQuantity(productId, newQuantity);
            orderDetail.setProductId(productId);
            orderDetail.setQuantity(requestedQuantity);
            orderDetail.setPrice(product.getPrice());
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);

        String vnpUrl = VNPAYConfig.createVNPayPaymentUrl(
                order.getId(),
                "Thanh toán đơn hàng #" + order.getId(),
                order.getTotalMoney(),
                "http://localhost:8084/orders/vnpay/return"
        );

        OrderResponse response = orderMapper.toOrderResponse(order);
        response.setPaymentUrl(vnpUrl); // Gán URL thanh toán
        return response;
    }

//    public float calculateShippingFee(String originAddress, String destinationAddress, float totalMoney) {
//        // Chuyển đổi địa chỉ thành tọa độ
//        String originCoordinates = distanceService.convertAddressToCoordinates(originAddress);
//        String destinationCoordinates = distanceService.convertAddressToCoordinates(destinationAddress);
//
//        // Tính khoảng cách
//        double distance = distanceService.calculateDistance(originCoordinates, destinationCoordinates);
//
//        // Tính phí giao hàng
//        if (totalMoney > 300 || distance < 2) {
//            return 0; // Miễn phí giao hàng
//        } else if (distance < 5) {
//            return 20; // Phí giao hàng 20
//        } else if (distance < 10) {
//            return 50; // Phí giao hàng 50
//        } else {
//            return 100; // Phí giao hàng 100
//        }
//    }
    public float calculateShippingFee(String address, Float totalMoney) {
        double distance = distanceService.calculateDistance(DEFAULT_ADDRESS, address);

        if (totalMoney != null && totalMoney > 300000) {
            return 0;
        }
        if (distance < 2) {
            return 0;
        } else if (distance < 5) {
            return 20000;
        } else if (distance < 10) {
            return 50000;
        } else {
            return 100000;
    }
}

    public OrderResponse getOrder(String id) {
        return orderMapper.toOrderResponse(
                orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED)));
    }


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
