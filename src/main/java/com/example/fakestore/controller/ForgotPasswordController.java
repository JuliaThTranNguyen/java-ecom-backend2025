package com.example.fakestore.controller;

import com.example.fakestore.dto.request.ChangePassword;
import com.example.fakestore.dto.request.VerifyEmailRequest;
import com.example.fakestore.dto.request.VerifyOtpRequest;
import com.example.fakestore.dto.response.ApiResponse;
import com.example.fakestore.service.ForgotPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/forgotPassword")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular frontend
@RequiredArgsConstructor
public class ForgotPasswordController {
    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/verifyEmail")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestBody @Valid VerifyEmailRequest emailRequest) {
        var response = forgotPasswordService.verifyEmail(emailRequest);

        ApiResponse<String> body = ApiResponse.<String>builder()
                .code(HttpStatus.OK)
                .result(response)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<ApiResponse<String>> verifyOtp(@RequestBody @Valid VerifyOtpRequest otpRequest) {
        var response = forgotPasswordService.verifyOtp(otpRequest);

        HttpStatus code = response.equals("OTP has expired!") ? HttpStatus.EXPECTATION_FAILED : HttpStatus.OK;

        ApiResponse<String> body =
                ApiResponse.<String>builder().code(code).result(response).build();

        return ResponseEntity.status(code).body(body);
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @RequestBody ChangePassword changePassword, @PathVariable String email) {
        var response = forgotPasswordService.changePassword(changePassword, email);

        HttpStatus code =
                response.equals("Please enter the password again") ? HttpStatus.EXPECTATION_FAILED : HttpStatus.OK;

        ApiResponse<String> body =
                ApiResponse.<String>builder().code(code).result(response).build();

        return ResponseEntity.status(code).body(body);
    }
}
