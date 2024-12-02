package com.cosmetics.order.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {

    String id;
    String orderId;
    String productId;
    Float price;
    Float quantity;
    Float totalMoney ;
}
