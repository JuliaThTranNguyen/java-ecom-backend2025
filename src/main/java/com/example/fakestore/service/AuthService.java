package com.example.fakestore.service;

import com.example.fakestore.dto.request.LoginDto;
import com.example.fakestore.dto.request.RegisterDto;
import com.example.fakestore.dto.request.TokenIdRequest;
import com.example.fakestore.dto.request.TokenRequest;
import com.example.fakestore.dto.response.ApiResponse;
import com.example.fakestore.dto.response.JwtAuthResponse;

public interface AuthService {
    ApiResponse<Object> register(RegisterDto registerDto);

    JwtAuthResponse login(LoginDto loginDto);

    JwtAuthResponse refreshToken(TokenIdRequest tokenIdRequest);

    void logout(TokenRequest request);
}
