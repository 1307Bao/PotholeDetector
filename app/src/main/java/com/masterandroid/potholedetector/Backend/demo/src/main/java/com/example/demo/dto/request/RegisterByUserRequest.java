package com.example.demo.dto.request;

import com.example.demo.validator.EmailConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterByUserRequest {
    @NotNull(message = "MISSING")
    String username;

    @NotNull(message = "MISSING")
    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;

    @NotNull(message = "MISSING")
    @EmailConstraint(message = "EMAIL_INVALID")
    String email;
}
