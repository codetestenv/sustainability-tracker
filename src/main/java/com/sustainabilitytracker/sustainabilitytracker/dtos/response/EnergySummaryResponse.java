package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class EnergySummaryResponse {
    private BigDecimal totalKwh;
    private BigDecimal totalRenewableKwh;
    private BigDecimal averageKwh;
    private String period;
    private Integer recordCount;
}
