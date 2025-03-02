package com.example.fakestore.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JwtAuthResponse {
    private String accessToken;
    private String tokenId;
    private static final String TOKEN_TYPE = "Bearer";
}
