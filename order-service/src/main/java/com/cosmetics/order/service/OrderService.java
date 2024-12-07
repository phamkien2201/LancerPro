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
    VNPAYConfig vnpayConfig;

    private static final String DEFAULT_ADDRESS = "Bến Nghé, Quận Nhất";

    public OrderResponse createOrder(OrderRequest request) {
        // Kiểm tra user
        ApiResponse<UserResponse> user = userClient.getUser(request.getUserId());
        if (user == null || user.getResult() == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Tính phí vận chuyển
        float shippingFee = calculateShippingFee(request.getAddress(), request.getTotalMoney());

        // Tạo order
        Order order = orderMapper.toOrder(request);
        order.setFee(shippingFee);

        // Kiểm tra phương thức thanh toán
        if (request.getPaymentMethod() == null) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        // Xử lý từng phương thức thanh toán
        switch (request.getPaymentMethod()) {
            case "COD":
                // Với COD, tạo order ngay lập tức
                order.setStatus(OrderStatus.PENDING);
                order = orderRepository.save(order);
                break;

            case "VNPAY":
                String vnpUrl = vnpayConfig.createVNPayPaymentUrl(
                        order.getId(),
                        "Thanh toán đơn hàng #" + order.getId(),
                        order.getTotalMoney(),
                        "http://localhost:8084/orders/vnpay/return"
                );

                // Chưa lưu order để đảm bảo chỉ lưu khi thanh toán thành công

                // Trả về response với URL thanh toán
                OrderResponse paymentResponse = orderMapper.toOrderResponse(order);
                paymentResponse.setPaymentUrl(vnpUrl);
                return paymentResponse;

            default:
                throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        // Xử lý chi tiết đơn hàng
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemRequest cartItemRequest : request.getCartItemsIdRequest()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getId());

            String productId = cartItemRequest.getProductId();
            Float requestedQuantity = cartItemRequest.getQuantity();

            ApiResponse<ProductResponse> productResponse = productClient.getProductById(productId);
            if (productResponse == null || productResponse.getResult() == null) {
                throw new AppException(ErrorCode.ORDER_NOT_EXISTED);
            }

            ProductResponse product = productResponse.getResult();
            Float currentQuantity = product.getQuantity();

            // Kiểm tra số lượng sản phẩm
            if (currentQuantity < requestedQuantity) {
                throw new RuntimeException("Insufficient quantity for product ID: " + productId);
            }

            // Cập nhật số lượng sản phẩm
            Float newQuantity = currentQuantity - requestedQuantity;
            productClient.updateProductQuantity(productId, newQuantity);

            orderDetail.setProductId(productId);
            orderDetail.setQuantity(requestedQuantity);
            orderDetail.setPrice(product.getPrice());
            orderDetails.add(orderDetail);
        }

        orderDetailRepository.saveAll(orderDetails);

        // Trả về response cho phương thức COD
        OrderResponse response = orderMapper.toOrderResponse(order);
        return response;
    }

    // Phương thức xác nhận thanh toán VNPAY
    public OrderResponse confirmVNPayPayment(String orderId, boolean paymentStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));

        if (paymentStatus) {
            // Thanh toán thành công
            order.setStatus(OrderStatus.PENDING);
            orderRepository.save(order);
            return orderMapper.toOrderResponse(order);
        } else {
            // Thanh toán thất bại
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public float calculateShippingFee(String address, Float totalMoney) {
        try {
            if (totalMoney != null && totalMoney > 300000) {
                return 0;
            }
            double distance = distanceService.calculateDistance(DEFAULT_ADDRESS, address);
            if (distance < 2) {
                return 0;
            } else if (distance < 5) {
                return 20000;
            } else if (distance < 10) {
                return 50000;
            } else {
                return 100000;
            }
        } catch (Exception e) {
            return 50000;
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
