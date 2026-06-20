package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.ReportRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.ReportResponse;
import com.sustainabilitytracker.sustainabilitytracker.services.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponse> generateReport(
            @Valid @RequestBody ReportRequest request,
            @RequestHeader("X-User-Id") Long currentUserId) {

        ReportResponse response = reportService.generateReport(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<ReportResponse>> getReportsByCompany(@PathVariable Long companyId) {
        List<ReportResponse> reports = reportService.getReportsByCompany(companyId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{reportId}/download")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long reportId) {
        byte[] fileBytes = reportService.downloadReport(reportId);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(fileBytes);
    }
}
