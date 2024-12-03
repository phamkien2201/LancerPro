package com.cosmetics.order.entity;

import com.cosmetics.order.dto.request.CartItemRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Getter
@Setter
@Builder
@Document(collection = "carts")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {
    @MongoId
    String id;

    String userId;

    List<CartItem> cartItemIds;

}
