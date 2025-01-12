package com.example.fakestore.controller;

import com.example.fakestore.dto.request.LoginDto;
import com.example.fakestore.dto.request.TokenIdRequest;
import com.example.fakestore.dto.request.TokenRequest;
import com.example.fakestore.dto.request.RegisterDto;
import com.example.fakestore.dto.response.ApiResponse;
import com.example.fakestore.dto.response.JwtAuthResponse;
import com.example.fakestore.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody @Valid RegisterDto registerDto) {

        return new ResponseEntity<>(authService.register(registerDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody @Valid LoginDto loginDto) {
        JwtAuthResponse response = authService.login(loginDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<JwtAuthResponse> refreshToken(@RequestBody @Valid TokenIdRequest tokenIdRequest) {
        JwtAuthResponse response = authService.refreshToken(tokenIdRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokenRequest request) {
        authService.logout(request);
        return ResponseEntity.ok().body("Logged out!");
    }
}
