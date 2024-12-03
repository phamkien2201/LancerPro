package com.cosmetics.order.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    String userId;
    String email;
    String name;
    String phoneNumber;
    String address;
    String status;
    Date orderDate;
    Float discount;
    Float fee;
    String note;
    Float totalMoney;
    String shippingMethod;
    LocalDate shippingDate;
    String trackingNumber;
    String paymentMethod;

    @Field("cart_items")
    List<CartItemRequest> cartItemsIdRequest;
}
