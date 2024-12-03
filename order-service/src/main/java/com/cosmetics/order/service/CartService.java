package com.cosmetics.order.service;

import com.cosmetics.order.dto.ApiResponse;
import com.cosmetics.order.dto.request.CartItemRequest;
import com.cosmetics.order.dto.response.CartResponse;
import com.cosmetics.order.dto.response.ProductResponse;
import com.cosmetics.order.dto.response.UserResponse;
import com.cosmetics.order.entity.Cart;
import com.cosmetics.order.entity.CartItem;
import com.cosmetics.order.mapper.CartMapper;
import com.cosmetics.order.repository.CartRepository;
import com.cosmetics.order.repository.httpclient.ProductClient;
import com.cosmetics.order.repository.httpclient.UserClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {

    CartRepository cartRepository;
    CartMapper cartMapper;
    UserClient userClient;
    ProductClient productClient;

    public CartResponse getCart(String userId) {
        ApiResponse<UserResponse> userResponse = userClient.getUser(userId);
        if (userResponse == null || userResponse.getResult() == null) {
            throw new RuntimeException("User not found for ID: " + userId);
        }
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = Cart.builder()
                    .userId(userId)
                    .cartItemIds(new ArrayList<>())
                    .build();
            cartRepository.save(cart);
        }
        // Map sang CartResponse
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse addProductToCart(String userId, CartItemRequest request) {

        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = Cart.builder()
                    .userId(userId)
                    .cartItemIds(new ArrayList<>())
                    .build();
            cartRepository.save(cart);
        }
        ProductResponse product = productClient.getProductById(request.getProductId()).getResult();

        boolean itemExists = cart.getCartItemIds().stream()
                .anyMatch(cartItem -> cartItem.getProductId().equals(request.getProductId()));
        if (itemExists) {
            throw new IllegalArgumentException("Product already in cart");
        }
        CartItem newCartItem = CartItem.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .price(product.getPrice())
                .build();

        cart.getCartItemIds().add(newCartItem);

        cartRepository.save(cart);
        return cartMapper.toCartResponse(cart);
    }

    public void clearCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            throw new IllegalArgumentException("Cart not found for userId: " + userId);
        }
        cart.getCartItemIds().clear();
        cartRepository.save(cart);
    }

    public void removeProductFromCart(String userId, String cartItemId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            throw new IllegalArgumentException("Cart not found for userId: " + userId);
        }
        cart.getCartItemIds().removeIf(item -> item.getId().equals(cartItemId));
        cartRepository.save(cart);
    }
}
