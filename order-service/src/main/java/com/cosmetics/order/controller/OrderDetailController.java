package com.cosmetics.order.controller;

import com.cosmetics.order.dto.ApiResponse;
import com.cosmetics.order.dto.request.OrderDetailRequest;
import com.cosmetics.order.dto.response.OrderDetailResponse;
import com.cosmetics.order.mapper.OrderDetailMapper;
import com.cosmetics.order.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-detail")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailController {
    OrderDetailService orderDetailService;

    @PostMapping("/create-order-detail")
    ApiResponse<OrderDetailResponse> createOrderDetail(@Valid @RequestBody OrderDetailRequest request) {
        return ApiResponse.<OrderDetailResponse>builder()
                .result(orderDetailService.createOrderDetail(request))
                .build();
    }

    @GetMapping("/{orderDetailId}")
    ApiResponse<OrderDetailResponse> getOrderDetail(@PathVariable("orderDetailId") String orderDetailId) {
        return ApiResponse.<OrderDetailResponse>builder()
                .result(orderDetailService.getOrderDetail(orderDetailId))
                .build();
    }

    @GetMapping("/get-orders/{order_id}")
    public ApiResponse<List<OrderDetailResponse>> getOrders(
            @Valid @PathVariable("order_id") String orderId) {
        return ApiResponse.<List<OrderDetailResponse>>builder()
                .result(orderDetailService.findByOrderId(orderId))
                .build();
    }
    @PutMapping("/{orderDetailId}")
    ApiResponse<OrderDetailResponse> updateOrderDetail(@PathVariable String orderDetailId, @RequestBody OrderDetailRequest request) {
        return ApiResponse.<OrderDetailResponse>builder()
                .result(orderDetailService.updateOrderDetail(orderDetailId, request))
                .build();
    }
    @DeleteMapping("/{id}")
    public void deleteOrderDetail(
            @Valid @PathVariable("id") String id
    ) {
        orderDetailService.deleteOrderDetail(id);
    }
}