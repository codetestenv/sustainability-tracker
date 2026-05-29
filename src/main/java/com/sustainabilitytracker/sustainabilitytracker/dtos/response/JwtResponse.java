package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String accessToken;

    public JwtResponse(String token) {
        accessToken=token;
    }
}
