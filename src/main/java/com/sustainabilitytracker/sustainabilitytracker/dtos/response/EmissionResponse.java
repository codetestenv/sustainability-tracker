package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmissionResponse {
    private Long id;
    private String companyName;
    private String departmentName;
    private String submittedByName;
    private String approvedByName;
    private BigDecimal co2Amount;
    private BigDecimal ch4Amount;
    private BigDecimal n2oAmount;
    private String scope;
    private String status;
    private String notes;
    private String rejectionReason;
    private LocalDate recordedAt;
    private Instant submittedAt;
    private Instant approvedAt;
}
