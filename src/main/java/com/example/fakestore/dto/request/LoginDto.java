package com.example.fakestore.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginDto {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
