package com.example.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class LoginByUserRequest {
    @NotNull(message = "MISSING")
    String email;
    @NotNull(message = "MISSING")
    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
}
