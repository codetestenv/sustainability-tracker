package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.EmissionRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.request.RejectRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.EmissionResponse;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.EmissionSummaryResponse;
import com.sustainabilitytracker.sustainabilitytracker.repositories.EmissionRepository;
import com.sustainabilitytracker.sustainabilitytracker.services.EmissionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/emissions")
@RequiredArgsConstructor
public class EmissionController {

    private final EmissionService emissionService;

    // SUBMIT EMISSION
    @PostMapping
    public ResponseEntity<EmissionResponse> submitEmission(
            @Valid @RequestBody EmissionRequest emissionRequest,
            UriComponentsBuilder uriBuilder) {

        EmissionResponse emissionResponse = emissionService.submitEmission(emissionRequest);

        var uri = uriBuilder.path("/emissions/{emissionId}")
                .buildAndExpand(emissionResponse.getId())
                .toUri();

        return ResponseEntity.created(uri).body(emissionResponse);
    }

    // SUBMIT FOR APPROVAL
    @PutMapping("/{emissionId}/submit")
    public ResponseEntity<EmissionResponse> submitForApproval(@PathVariable Long emissionId) {
        EmissionResponse response = emissionService.submitForApproval(emissionId);
        return ResponseEntity.ok(response);
    }

    // APPROVE EMISSION
    @PutMapping("/{emissionId}/approve")
    public ResponseEntity<EmissionResponse> approveEmission(@PathVariable Long emissionId) {
        EmissionResponse response = emissionService.approveEmission(emissionId);
        return ResponseEntity.ok(response);
    }

    // REJECT EMISSION
    @PutMapping("/{emissionId}/reject")
    public ResponseEntity<EmissionResponse> rejectEmission(
            @PathVariable Long emissionId,
            @Valid @RequestBody RejectRequest request) {

        EmissionResponse response = emissionService.rejectEmission(emissionId, request.getReason());
        return ResponseEntity.ok(response);
    }

    // GET EMISSIONS BY COMPANY
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<EmissionResponse>> getEmissionsByCompany(@PathVariable Long companyId) {
        List<EmissionResponse> emissions = emissionService.getEmissionByCompany(companyId);
        return ResponseEntity.ok(emissions);
    }

    // GET EMISSION SUMMARY
    @GetMapping("/company/{companyId}/summary")
    public ResponseEntity<EmissionSummaryResponse> getEmissionSummary(
            @PathVariable Long companyId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        LocalDate now = LocalDate.now();

        LocalDate start = (startDate != null) ? startDate : now.minusDays(30);
        LocalDate end = (endDate != null) ? endDate : now;

        EmissionSummaryResponse summaryResponse = emissionService
                .getEmissionSummary(companyId, start, end);

        return ResponseEntity.ok(summaryResponse);
    }
}