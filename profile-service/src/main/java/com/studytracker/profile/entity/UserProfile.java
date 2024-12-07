package com.studytracker.profile.entity;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "user_profile")
public class UserProfile {

    @Id
    String id = UUID.randomUUID().toString(); // Tự động sinh UUID cho MongoDB
    String userId;
    String username;
    String email;
    String name;
    LocalDate dob;
    String gender;
    Integer age;
    String phoneNumber;
}
