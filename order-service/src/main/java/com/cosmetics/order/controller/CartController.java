package com.cosmetics.order.controller;

import com.cosmetics.order.dto.ApiResponse;
import com.cosmetics.order.dto.request.CartItemRequest;
import com.cosmetics.order.dto.response.CartResponse;
import com.cosmetics.order.dto.response.OrderResponse;
import com.cosmetics.order.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    CartService cartService;

    @GetMapping("/{userId}")
    public ApiResponse<CartResponse> getCart(@PathVariable String userId) {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.getCart(userId))
                .build();
    }

    @PostMapping("/{userId}/items")
    public ApiResponse<CartResponse> addProductToCart(
            @PathVariable String userId,
            @RequestBody CartItemRequest request
    ) {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.addProductToCart(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}/clear")
    public void clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
    }

    @DeleteMapping("/{userId}/items/{cartItemId}")
    public void removeProductFromCart(
            @PathVariable String userId,
            @PathVariable String cartItemId
    ) {
        cartService.removeProductFromCart(userId, cartItemId);
    }
}
