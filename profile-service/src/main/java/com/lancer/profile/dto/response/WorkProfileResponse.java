package com.lancer.profile.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkProfileResponse {

    String id;
    String userId;
    boolean isClient;
    String title;
    String introduce;
    String personalWebsite;
    String areaOfExpertise;
    String qualification;
    List<String> keySkills;
    List<String> listOfServices;
    boolean active;
    boolean isFullTime;
}