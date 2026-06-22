package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.RegisterUserRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.UserResponse;
import com.sustainabilitytracker.sustainabilitytracker.enums.Role;
import com.sustainabilitytracker.sustainabilitytracker.mappers.UserMapper;
import com.sustainabilitytracker.sustainabilitytracker.repositories.UserRepository;
import com.sustainabilitytracker.sustainabilitytracker.services.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("Getting all users");
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder) {

        log.info("Registering new user with email: {}",
                request.getEmail());

        UserResponse userResponse = authService.create(request);

        var uri = uriBuilder
                .path("/users/{id}")
                .buildAndExpand(userResponse.getId())
                .toUri();

        return ResponseEntity.created(uri).body(userResponse);
    }
}