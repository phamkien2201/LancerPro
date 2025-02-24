package com.lancer.project.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Field;

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
    String category;
    BigDecimal budgetMin;
    BigDecimal budgetMax;
    List<String> skills;
    String address;
    String paymentMethod;
    String workPattern;
    String clientId;
    String status;
    Instant deadline;
}
