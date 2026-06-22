package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.config.JwtProperties;
import com.sustainabilitytracker.sustainabilitytracker.dtos.request.ChangePasswordRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.request.LoginRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.LoginResponse;
import com.sustainabilitytracker.sustainabilitytracker.repositories.UserRepository;
import com.sustainabilitytracker.sustainabilitytracker.security.JwtTokenProvider;
import com.sustainabilitytracker.sustainabilitytracker.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(request, response));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {
        return ResponseEntity.ok(authService.refresh(refreshToken, response));
    }

}