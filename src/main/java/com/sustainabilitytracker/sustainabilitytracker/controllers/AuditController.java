package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.AuditRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.AuditResponse;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.ReportResponse;
import com.sustainabilitytracker.sustainabilitytracker.services.AuditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audits")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/pending")
    public ResponseEntity<List<ReportResponse>> getReportsForAudit() {
        List<ReportResponse> reports = auditService.getReportsForAudit();
        return ResponseEntity.ok(reports);
    }

    @PutMapping("/reports/{reportId}/review")
    public ResponseEntity<AuditResponse> reviewReport(
            @PathVariable Long reportId,
            @Valid @RequestBody AuditRequest request) {

        AuditResponse response = auditService.reviewReport(reportId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports/{reportId}/history")
    public ResponseEntity<List<AuditResponse>> getAuditHistory(@PathVariable Long reportId) {
        List<AuditResponse> history = auditService.getAuditHistory(reportId);
        return ResponseEntity.ok(history);
    }
}
