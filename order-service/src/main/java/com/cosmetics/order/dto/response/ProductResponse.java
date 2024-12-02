package com.cosmetics.order.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    Float price;
    Float quantity;
    String description;
    List<String> thumbnails;
    List<String> ingredient;
    String userManual;
    String categoryId;
    String brandId;
}
