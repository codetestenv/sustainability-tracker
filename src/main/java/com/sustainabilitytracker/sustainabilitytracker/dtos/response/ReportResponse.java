package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import com.sustainabilitytracker.sustainabilitytracker.enums.AuditStatus;
import com.sustainabilitytracker.sustainabilitytracker.enums.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private Long id;
    private Long companyId;
    private String companyName;
    private Long scoreId;
    private String reportTitle;
    private ReportType reportType;
    private String fileFormat;
    private String downloadUrl;
    private AuditStatus auditStatus;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private Instant generatedAt;
}