package com.lancer.project.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BidResponse {
    String id;
    String projectId;
    String freelancerId;
    BigDecimal price;
    String proposal;
    LocalDateTime expectedTime;
    String phoneNumber;
    String email;
    String status;
}
