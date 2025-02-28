package com.lancer.bid.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectResponse {
    String id;
    String title;
    String description;
    String categoryId;
    BigDecimal budgetMin;
    BigDecimal budgetMax;
    List<String> skills;
    String address;
    String email;
    String paymentMethod;
    String workPattern;
    String clientId;
    String status;
    Instant deadline;
}
