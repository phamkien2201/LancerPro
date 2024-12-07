package com.studytracker.identity.dto.request;

import java.time.LocalDate;

import com.studytracker.identity.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationRequest {

    String userId;
    String username;
    String email;
    String name;
    String gender;
    Integer age;
    String phoneNumber;

    @DobConstraint(min = 10, message = "INVALID_DOB")
    LocalDate dob;
}
