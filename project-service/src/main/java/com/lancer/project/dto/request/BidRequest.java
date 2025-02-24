package com.lancer.project.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BidRequest {

    String projectId;
    String freelancerId;
    BigDecimal price;
    String proposal;
    LocalDateTime expectedTime;
    String phoneNumber;
    String email;
    String status;
}
