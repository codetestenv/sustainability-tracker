package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.RegisterUserRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.UserResponse;
import com.sustainabilitytracker.sustainabilitytracker.enums.Role;
import com.sustainabilitytracker.sustainabilitytracker.mappers.UserMapper;
import com.sustainabilitytracker.sustainabilitytracker.repositories.UserRepository;
import com.sustainabilitytracker.sustainabilitytracker.services.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final AuthService authService;
//    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<?> getAllUser(){
        System.out.println("CLICKED!!");
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(
            UriComponentsBuilder uriBuilder,
            @Valid @RequestBody RegisterUserRequest request){
        var userResponse = authService.create(request);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userResponse.getId()).toUri();
        return ResponseEntity.created(uri).body(userResponse);
    }
}
