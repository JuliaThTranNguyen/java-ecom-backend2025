package com.example.fakestore.security.jwt;

import com.example.fakestore.entity.RefreshToken;
import com.example.fakestore.exception.ApiException;
import com.example.fakestore.exception.ErrorCode;
import com.example.fakestore.repository.InvalidatedTokenRepository;
import com.example.fakestore.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    @NonFinal
    @Value("${jwt.secretKey}")
    private String JWT_SECRET;

    @NonFinal
    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    // generate JWT token from User entity
    public String generateTokenFromEmail(String email) {
        Date currentDate = new Date();
        Date expirationDate = new Date(
                Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli());

        return Jwts.builder()
                .claim("uuid", UUID.randomUUID().toString())
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(key(JWT_SECRET))
                .compact();
    }

    private Key key(String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    // get username from JWT token
    public String getEmailFromToken(String token) {
        return getJwtTokenClaims(token).getSubject();
    }

    public Claims getJwtTokenClaims(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key(JWT_SECRET))
                .build()
                .parseSignedClaims(token)
                .getBody();

        return claims;
    }

    // validate Jwt token
    public boolean verifyToken(String token) {
        try {
            Claims claims = getJwtTokenClaims(token);

            // check whether token was already invalid or not
            if (invalidatedTokenRepository.existsById(claims.get("uuid", String.class))) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "You already logged out!");
            }

            return true;

        } catch (ExpiredJwtException e) {
            throw new ApiException(ErrorCode.UNAUTHENTICATED, "Token is expired!");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new ApiException(ErrorCode.BAD_REQUEST, "Invalid JWT token!");
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw new ApiException(ErrorCode.BAD_REQUEST, "Unsupported JWT token!");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw new ApiException(ErrorCode.BAD_REQUEST, "JWT claims string is empty!");
        }
    }

    public boolean verifyRefreshToken(String tokenId){

        RefreshToken refreshToken = refreshTokenRepository.findByTokenId(tokenId).orElseThrow(
                () -> new ApiException(ErrorCode.BAD_REQUEST, "Token not found")
        );

        Date expiryTime = refreshToken.getExpirationTime();

        if (invalidatedTokenRepository.existsById(tokenId) || expiryTime.before(new Date())){

            throw new ApiException(ErrorCode.UNAUTHENTICATED, "Token is expired!");
        }

        return true;
    }


}
