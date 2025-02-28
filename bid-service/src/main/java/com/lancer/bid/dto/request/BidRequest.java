package com.lancer.bid.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
