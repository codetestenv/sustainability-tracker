package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import com.sustainabilitytracker.sustainabilitytracker.entities.Company;
import com.sustainabilitytracker.sustainabilitytracker.entities.SustainabilityScore;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class AdminDashboardResponse {

    private Long totalCompanies;
    private Long totalUsers;
    private Long totalReports;

    private SustainabilityScore bestScore;
    private SustainabilityScore worstScore;

    private Map<String, Object> systemStats;
    private List<Company> topCompanies;
}
