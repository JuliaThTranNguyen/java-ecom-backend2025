package com.example.fakestore.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class VerifyOtpRequest {
    @NotEmpty
    private String email;
    @NotNull
    private Integer otp;
}
