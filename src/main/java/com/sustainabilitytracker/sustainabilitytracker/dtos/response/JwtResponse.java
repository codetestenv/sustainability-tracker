package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String refreshToken;
    private String accessToken;
}
