package com.example.fakestore.service.impl;

import com.example.fakestore.dto.request.LoginDto;
import com.example.fakestore.dto.request.RegisterDto;
import com.example.fakestore.dto.request.TokenIdRequest;
import com.example.fakestore.dto.request.TokenRequest;
import com.example.fakestore.dto.response.ApiResponse;
import com.example.fakestore.dto.response.JwtAuthResponse;
import com.example.fakestore.entity.InvalidatedToken;
import com.example.fakestore.entity.RefreshToken;
import com.example.fakestore.entity.Role;
import com.example.fakestore.entity.User;
import com.example.fakestore.exception.ApiException;
import com.example.fakestore.exception.ErrorCode;
import com.example.fakestore.repository.InvalidatedTokenRepository;
import com.example.fakestore.repository.RefreshTokenRepository;
import com.example.fakestore.repository.UserRepository;
import com.example.fakestore.security.SecurityConfig;
import com.example.fakestore.security.jwt.JwtTokenProvider;
import com.example.fakestore.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    @Override
    public ApiResponse register(RegisterDto registerDto) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(registerDto.getEmail()))) {
            log.error("Email {} already exists", registerDto.getEmail());
            throw new ApiException(ErrorCode.BAD_REQUEST, "Email already exists");
        }

        User newUser = User.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .email(registerDto.getEmail())
                .password(SecurityConfig.passwordEncoder().encode(registerDto.getPassword()))
                .role(Role.ROLE_CUSTOMER)
                .build();

        userRepository.save(newUser);

        return ApiResponse.builder()
                .code(HttpStatus.CREATED)
                .result("User registered successfully")
                .build();
    }

    @Override
    public JwtAuthResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "User not found")
        );

        var existingRefreshToken = refreshTokenRepository.findByUser(user);
        if (existingRefreshToken.isPresent()) {
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(existingRefreshToken.get().getTokenId())
                    .expiryTime(existingRefreshToken.get().getExpirationTime())
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
            refreshTokenRepository.deleteByTokenId(existingRefreshToken.get().getTokenId());
        }


        String accessToken = jwtTokenProvider.generateTokenFromEmail(authentication.getName());

        // save refresh token
        Claims claims = jwtTokenProvider.getJwtTokenClaims(accessToken);
        String tokenId = claims.get("uuid", String.class);


        RefreshToken refreshToken = RefreshToken.builder()
                .tokenId(tokenId)
                .expirationTime(new Date(claims.getIssuedAt().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .user(user)
                .build();

        refreshTokenRepository.save(refreshToken);

        return JwtAuthResponse.builder()
                .accessToken(accessToken)
                .tokenId(tokenId)
                .build();
    }

    @Override
    public JwtAuthResponse refreshToken(TokenIdRequest request) {
        jwtTokenProvider.verifyRefreshToken(request.getTokenId());

        RefreshToken refreshToken = refreshTokenRepository.findByTokenId(request.getTokenId()).orElseThrow(
                () -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Token not found")
        );

        refreshTokenRepository.deleteByTokenId(request.getTokenId());

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(refreshToken.getTokenId())
                .expiryTime(refreshToken.getExpirationTime())
                .build();
        invalidatedTokenRepository.save(invalidatedToken);

        String accessToken = jwtTokenProvider.generateTokenFromEmail(refreshToken.getUser().getEmail());
        Claims newClaims = jwtTokenProvider.getJwtTokenClaims(accessToken);

        return JwtAuthResponse.builder()
                .accessToken(accessToken)
                .tokenId(newClaims.get("uuid", String.class))
                .build();
    }

    @Override
    public void logout(TokenRequest request) {
        jwtTokenProvider.verifyToken(request.getToken());

        Claims claims = jwtTokenProvider.getJwtTokenClaims(request.getToken());

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(claims.get("uuid", String.class))
                .expiryTime(claims.getExpiration())
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }
}
