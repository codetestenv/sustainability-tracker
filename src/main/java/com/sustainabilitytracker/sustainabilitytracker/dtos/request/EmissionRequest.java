package com.sustainabilitytracker.sustainabilitytracker.dtos.request;

import com.sustainabilitytracker.sustainabilitytracker.enums.EmissionScope;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class EmissionRequest {
    @NotNull
    private Long companyId;

    @NotNull
    private Long departmentId;

    @NotNull
    @Min(0)
    private BigDecimal co2Amount;

    @Min(0)
    private BigDecimal ch$Amount;

    @Min(0)
    private BigDecimal n2oAmount;

    @NotNull
    private EmissionScope scope;

    private String notes;

    @NotNull
    private Instant createdAt;
}