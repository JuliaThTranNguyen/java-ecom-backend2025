package com.example.fakestore.service;

import com.example.fakestore.dto.request.ChangePassword;
import com.example.fakestore.dto.request.VerifyEmailRequest;
import com.example.fakestore.dto.request.VerifyOtpRequest;

public interface ForgotPasswordService {
    String verifyEmail(VerifyEmailRequest emailRequest);

    String verifyOtp(VerifyOtpRequest otpRequest);

    String changePassword(ChangePassword changePassword, String email);
}
