package com.lancer.project.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "Bids")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bid extends BaseEntity{

    @Id
    String id;

    @Field("project_id")
    String projectId;

    @Field("freelancer_id")
    String freelancerId;

    BigDecimal price;

    String proposal;

    @Field("expected_time")
    LocalDateTime expectedTime;

    String phoneNumber;

    String email;

    @Field("status")
    String status;
}
