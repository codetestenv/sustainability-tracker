package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import com.sustainabilitytracker.sustainabilitytracker.entities.SustainabilityScore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDashboardResponse {

    private long totalCompanies;
    private long totalUsers;

    private SustainabilityScore bestScore;
    private SustainabilityScore worstScore;

    private SystemStatsResponse stats;
}
