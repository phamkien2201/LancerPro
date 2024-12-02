package com.cosmetics.order.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@Document(collection = "orders")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @MongoId
    String id;
    String userId;
    String name;
    String email;

    @Field("phone_number")
    String phoneNumber;

    String address;

    String note;

    @Field("order_date")
    Date orderDate;

    String status;

    @Field("total_money")
    Float totalMoney;

    @Field("shipping_method")
    String shippingMethod;

    @Field("shipping_address")
    String shippingAddress;

    @Field("shipping_date")
    LocalDate shippingDate;

    @Field("tracking_number")
    String trackingNumber;

    @Field("payment_method")
    String paymentMethod;

    Boolean active;//danh cho admin

    @Field("order_details")
    List<OrderDetail> orderDetailId;
}
