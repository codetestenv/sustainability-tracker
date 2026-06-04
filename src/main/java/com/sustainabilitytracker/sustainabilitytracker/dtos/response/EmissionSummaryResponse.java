package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmissionSummaryResponse {
    private BigDecimal totalCO2;
    private BigDecimal totalCH4;
    private BigDecimal totalN2O;
    private BigDecimal totalEmissions;
    private String period;
    private Integer recordCount;

}
