package com.lancer.bid.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    LocalDate dob;
    String email;
    boolean emailVerified;
    String password;
    String name;
    String gender;
    Integer age;
    String occupation;
}
