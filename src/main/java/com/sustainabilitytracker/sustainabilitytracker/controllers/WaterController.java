package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.WaterRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.WaterResponse;
import com.sustainabilitytracker.sustainabilitytracker.repositories.WaterRepository;
import com.sustainabilitytracker.sustainabilitytracker.services.WaterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("")
public class WaterController {

    private final WaterService waterService;
    private final WaterRepository waterRepository;

    @PostMapping
    public ResponseEntity<WaterResponse> submitWater(
            @Valid @RequestBody WaterRequest waterRequest,
            UriComponentsBuilder uriBuilder) {

        WaterResponse waterResponse = waterService.submitWater(waterRequest);

        var uri = uriBuilder.path("/water/{waterId}")
                .buildAndExpand(waterResponse.getId())
                .toUri();

        return ResponseEntity.created(uri).body(waterResponse);
    }


}
