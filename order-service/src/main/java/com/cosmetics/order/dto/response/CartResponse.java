package com.cosmetics.order.dto.response;

import com.cosmetics.order.entity.CartItem;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {

    String id;

    String userId;

    List<CartItem> cartItemIds;

}
