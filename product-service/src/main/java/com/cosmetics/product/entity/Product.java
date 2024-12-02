package com.cosmetics.product.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.List;

@Document(collection = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity {

    @Id
    String id;

    @Field("name")
    String name;
    Float price;
    Float quantity;

    @Field("description")
    String description;

    @Field("thumbnails")
    List<String> thumbnails;

    @Field("ingredient")
    List<String> ingredient;

    @Field("user_manual")
    String userManual;

    @Field("category_id")
    String categoryId;

    @Field("brand_id")
    String brandId;
}
