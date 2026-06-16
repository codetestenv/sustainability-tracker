package com.sustainabilitytracker.sustainabilitytracker.dtos.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GovernanceRequest {

    @NotNull(message = "Company is required")
    private Long companyId;

    @NotNull(message = "Compliance score is required")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private BigDecimal complianceScore;

    @NotNull(message = "Policy count is required")
    @Min(value = 0)
    private Integer policyCount;

    @Min(value = 0)
    private Integer violationsCount;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private BigDecimal boardDiversityPct;

    private Boolean ethicsTrainingDone;

    private String notes;

    @NotNull(message = "Recorded date is required")
    private LocalDate recordedAt;
}
