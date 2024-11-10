package com.studytracker.identity.dto.request;

import com.studytracker.identity.validator.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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
    String occupation;


    @DobConstraint(min = 10, message = "INVALID_DOB")
    LocalDate dob;
}
