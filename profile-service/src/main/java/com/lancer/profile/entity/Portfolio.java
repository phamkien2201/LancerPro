package com.lancer.profile.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "portfolio")
public class Portfolio {

    @Id
    String id = UUID.randomUUID().toString();

    String userId;
    String title;
    String Attachments;
    String description;
    List<String> relatedServices;
    String urlServices;
}