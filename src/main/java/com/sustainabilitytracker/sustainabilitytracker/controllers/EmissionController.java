package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.EmissionRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.EmissionResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emissions")
public class EmissionController {
    @PostMapping
    public ResponseEntity<EmissionResponse> submitEmission(
            @Valid
            @RequestBody EmissionRequest emissionRequest){
        return ResponseEntity.ok().build();
    }
}
