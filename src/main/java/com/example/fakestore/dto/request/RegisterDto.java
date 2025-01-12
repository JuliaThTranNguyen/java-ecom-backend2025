package com.example.fakestore.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterDto {
    @NotEmpty
    String firstName;
    @NotEmpty
    String lastName;
    @Email
    @NotEmpty
    String email;
    @NotEmpty
    String password;

}
