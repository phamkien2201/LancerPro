package com.lancer.bid.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "bids")
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
