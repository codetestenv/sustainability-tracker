package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;


@Data
public class EnergyResponse {
    private Long id;
    private String companyName;
    private String submittedByName;
    private String approvedByName;
    private BigDecimal totalKwh;
    private BigDecimal renewableKwh;
    private String status;
    private String notes;
    private String rejectionReason;
    private LocalDate recordedAt;
    private Instant submittedAt;
    private Instant approvedAt;
}
