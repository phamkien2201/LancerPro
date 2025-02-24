package com.lancer.profile.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "work_profile")
public class WorkProfile {

    @Id
    String id = UUID.randomUUID().toString();

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