package com.sustainabilitytracker.sustainabilitytracker.dtos.request;

import com.sustainabilitytracker.sustainabilitytracker.enums.EmissionScope;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmissionRequest {
    @NotNull
    private Long companyId;

    @NotNull
    private Long departmentId;

    @NotNull(message = "CO2 amount is required")
    @DecimalMin(value = "0.0", message = "CO2 amount cannot be negative")
    private BigDecimal co2Amount;

    @DecimalMin(value = "0.0", message = "CH4 amount cannot be negative")
    private BigDecimal ch4Amount;

    @DecimalMin(value = "0.0", message = "N2O amount cannot be negative")
    private BigDecimal n2oAmount;

    @NotNull
    private EmissionScope scope;

    private String notes;

    @NotNull
    private LocalDate recordedAt;

}