package com.cosmetics.order.service;

import com.cosmetics.order.entity.OrderDetail;
import com.cosmetics.order.repository.OrderDetailRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailService {

    OrderDetailRepository orderDetailRepository;

    public OrderDetail getOrderDetail(String id) {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find OrderDetail"));
    }

}
