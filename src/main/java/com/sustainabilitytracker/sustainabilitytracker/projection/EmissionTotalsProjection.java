package com.sustainabilitytracker.sustainabilitytracker.projection;

import java.math.BigDecimal;

public interface EmissionTotalsProjection {
    BigDecimal getTotalCO2();
    BigDecimal getTotalCH4();
    BigDecimal getTotalN2O();
    Long getRecordCount();
}
