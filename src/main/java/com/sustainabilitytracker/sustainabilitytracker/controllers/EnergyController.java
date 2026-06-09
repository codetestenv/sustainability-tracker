package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.EnergyRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.request.RejectRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.EnergyResponse;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.EnergySummaryResponse;
import com.sustainabilitytracker.sustainabilitytracker.repositories.EnergyRepository;
import com.sustainabilitytracker.sustainabilitytracker.services.EnergyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@AllArgsConstructor
@Controller
public class EnergyController {

    private final EnergyService energyService;

    // SUBMIT ENERGY
    @PostMapping
    public ResponseEntity<EnergyResponse> submitEnergy(
            @Valid @RequestBody EnergyRequest energyRequest,
            UriComponentsBuilder uriBuilder) {

        EnergyResponse energyResponse = energyService.submitEnergy(energyRequest);

        var uri = uriBuilder.path("/energy/{energyId}")
                .buildAndExpand(energyResponse.getId())
                .toUri();

        return ResponseEntity.created(uri).body(energyResponse);
    }


}
