package com.lancer.profile.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PortfolioRequest {

    String userId;
    String title;
    String Attachments;
    String description;
    List<String> relatedServices;
    String urlServices;
}