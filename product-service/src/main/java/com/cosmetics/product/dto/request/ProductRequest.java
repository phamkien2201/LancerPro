package com.cosmetics.product.dto.request;

import com.cosmetics.product.entity.Brand;
import com.cosmetics.product.entity.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {

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
