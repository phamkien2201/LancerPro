package com.lancer.profile.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkProfileRequest {

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