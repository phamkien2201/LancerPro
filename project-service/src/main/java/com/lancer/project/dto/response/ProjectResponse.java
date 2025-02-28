package com.lancer.project.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
