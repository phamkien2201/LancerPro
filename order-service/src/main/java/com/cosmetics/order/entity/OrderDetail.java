package com.cosmetics.order.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Builder
@Document(collection = "order_details")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail {
    @Id
    String id;

    @Field("order_id")
    String orderId;

    @Field("product_id")
    String productId;

    Float price;

    Float quantity;

    @Field("total_money")
    Float totalMoney ;

}
