package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
public class WaterResponse {
    private Long id;
    private String companyName;
    private String submittedByName;
    private String approvedByName;

    private BigDecimal consumedLiters;
    private BigDecimal recycledLiters;
    private String source;

    private String status;
    private String notes;
    private String rejectionReason;
    private Instant submittedAt;
    private Instant approvedAt;
    private LocalDate recordedAt;
}
