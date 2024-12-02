package com.cosmetics.order.repository;

import com.cosmetics.order.entity.OrderDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderDetailRepository extends MongoRepository<OrderDetail, String> {

    List<OrderDetail> findByOrderId(String orderId);
}
