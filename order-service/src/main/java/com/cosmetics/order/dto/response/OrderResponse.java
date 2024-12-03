package com.cosmetics.order.dto.response;

import com.cosmetics.order.entity.OrderDetail;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    String id;
    String userId;
    String name;
    String email;
    String phoneNumber;
    String address;
    String note;
    Date orderDate;
    String status;
    Float discount;
    Float fee;
    Float totalMoney;
    String shippingMethod;
    LocalDate shippingDate;
    String trackingNumber;
    String paymentMethod;
    Boolean active;
    List<OrderDetail> orderDetailId;
    String paymentUrl;
}
