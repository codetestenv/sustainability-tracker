package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.RejectRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.request.SocialRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.SocialResponse;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.SocialSummaryResponse;
import com.sustainabilitytracker.sustainabilitytracker.services.SocialService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/social")
public class SocialController {

    private final SocialService socialService;

    @PostMapping
    public ResponseEntity<SocialResponse> submitSocial(
            @Valid @RequestBody SocialRequest request,
            UriComponentsBuilder uriBuilder) {

        SocialResponse response = socialService.submitSocial(request);
        var uri = uriBuilder.path("/social/{socialId}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{socialId}/submit")
    public ResponseEntity<SocialResponse> submitForApproval(@PathVariable Long socialId) {
        return ResponseEntity.ok(socialService.submitForApproval(socialId));
    }

    @PutMapping("/{socialId}/approve")
    public ResponseEntity<SocialResponse> approveSocial(@PathVariable Long socialId) {
        return ResponseEntity.ok(socialService.approveSocial(socialId));
    }

    @PutMapping("/{socialId}/reject")
    public ResponseEntity<SocialResponse> rejectSocial(
            @PathVariable Long socialId,
            @Valid @RequestBody RejectRequest request) {
        return ResponseEntity.ok(socialService.rejectSocial(socialId, request.getReason()));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<SocialResponse>> getSocialByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(socialService.getSocialByCompany(companyId));
    }

    @GetMapping("/company/{companyId}/summary")
    public ResponseEntity<SocialSummaryResponse> getSocialSummary(
            @PathVariable Long companyId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        LocalDate now = LocalDate.now();
        Instant start = startDate != null ? startDate.atStartOfDay(ZoneOffset.UTC).toInstant()
                : now.minusDays(30).atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = endDate != null ? endDate.atTime(23, 59, 59).toInstant(ZoneOffset.UTC)
                : now.atTime(23, 59, 59).toInstant(ZoneOffset.UTC);

        return ResponseEntity.ok(socialService.getSocialSummary(companyId, startDate, endDate));
    }
}
