package com.cosmetics.order.dto.request;

import com.cosmetics.order.entity.CartItem;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartRequest {

    String userId;

    List<CartItem> cartItemIds;

}
