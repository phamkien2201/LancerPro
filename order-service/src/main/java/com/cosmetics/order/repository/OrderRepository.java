package com.cosmetics.order.repository;

import com.cosmetics.order.dto.response.OrderResponse;
import com.cosmetics.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<OrderResponse> findByUserId(String userId);

    Page<Order> findAll(Pageable pageable);
}
