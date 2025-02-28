package com.lancer.project.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Document(collection = "projects")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project extends BaseEntity{

    @Id
    String id;

    String title;

    String description;

    String categoryId;

    @Field("budget_min")
    BigDecimal budgetMin;

    @Field("budget_max")
    BigDecimal budgetMax;

    List<String> skills;

    String address;

    String email;

    @Field("payment_method")
    String paymentMethod;

    @Field("work_pattern")
    String workPattern;

    @Field("client_id")
    String clientId;

    @Field("status")
    String status;

    Instant deadline;

}
