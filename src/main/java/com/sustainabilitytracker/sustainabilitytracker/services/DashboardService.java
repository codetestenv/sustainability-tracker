package com.sustainabilitytracker.sustainabilitytracker.services;

import com.sustainabilitytracker.sustainabilitytracker.dtos.response.AdminDashboardResponse;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.DashboardResponse;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.SystemStatsResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.Company;
import com.sustainabilitytracker.sustainabilitytracker.entities.SustainabilityScore;
import com.sustainabilitytracker.sustainabilitytracker.entities.User;
import com.sustainabilitytracker.sustainabilitytracker.enums.AuditStatus;
import com.sustainabilitytracker.sustainabilitytracker.enums.Role;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.AccessDeniedException;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.ResourceNotFoundException;
import com.sustainabilitytracker.sustainabilitytracker.repositories.*;
import com.sustainabilitytracker.sustainabilitytracker.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final SustainabilityScoreRepository scoreRepository;
    private final EmissionRepository emissionRepository;
    private final EnergyRepository energyRepository;
    private final WaterRepository waterRepository;
    private final WasteRepository wasteRepository;
    private final ReportRepository reportRepository;
    private final DashboardRepository dashboardRepository;

    @Transactional(readOnly = true)
    public DashboardResponse getCompanyDashboard(Long companyId) {

        User currentUser = SecurityUtils.getCurrentUser();

        if (!SecurityUtils.hasAccessToCompany(currentUser, companyId)) {
            throw new AccessDeniedException("You do not have access to this company's dashboard");
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));

        // Latest Score
        SustainabilityScore latestScore = scoreRepository
                .findTopByCompanyIdOrderByCalculatedAtDesc(companyId).orElse(null);

        // Score History (last 6 months)
        List<SustainabilityScore> scoreHistory = scoreRepository
                .findTop6ByCompanyIdOrderByPeriodStartDesc(companyId);

        LocalDate now = LocalDate.now();
        LocalDate monthStart = now.withDayOfMonth(1);

        // Key Metrics - Current Month
        BigDecimal totalCo2 = emissionRepository.getTotalCo2(companyId, monthStart, now);
        BigDecimal totalEnergy = energyRepository.getTotalKwh(companyId, monthStart, now);
        BigDecimal totalWater = waterRepository.getTotalConsumedLiters(companyId, monthStart, now);
        BigDecimal totalWaste = wasteRepository.getTotalKg(companyId, monthStart, now);

        // Pending Items - Optimized single query
        int pendingApprovals = dashboardRepository.countAllPendingByCompanyId(companyId);
        int pendingReports = (int) reportRepository.countByCompanyIdAndAuditStatus(companyId, AuditStatus.PENDING);

        return DashboardResponse.builder()
                .companyId(company.getId())
                .companyName(company.getName())
                .latestScore(latestScore)
                .scoreHistory(scoreHistory)
                .totalCo2(totalCo2 != null ? totalCo2 : BigDecimal.ZERO)
                .totalEnergyKwh(totalEnergy != null ? totalEnergy : BigDecimal.ZERO)
                .totalWaterLiters(totalWater != null ? totalWater : BigDecimal.ZERO)
                .totalWasteKg(totalWaste != null ? totalWaste : BigDecimal.ZERO)
                .pendingApprovals(pendingApprovals)
                .pendingReports(pendingReports)
                .unreadNotifications(0) // TODO: Integrate NotificationService later
                .build();
    }

    @Transactional(readOnly = true)
    public AdminDashboardResponse getAdminDashboard() {

        long totalCompanies = companyRepository.countByIsActiveTrue();
        long totalUsers = userRepository.countByIsActiveTrue();
        long totalReports = reportRepository.count();
        long pendingAudits = reportRepository.countByAuditStatus(AuditStatus.PENDING);
        long activeCompanies = companyRepository.countByIsActiveTrue();

        SustainabilityScore bestScore = scoreRepository.findTopByOrderByTotalScoreDesc().orElse(null);
        SustainabilityScore worstScore = scoreRepository.findTopByOrderByTotalScoreAsc().orElse(null);

        SystemStatsResponse stats = SystemStatsResponse.builder()
                .totalCompanies(totalCompanies)
                .totalUsers(totalUsers)
                .totalReports(totalReports)
                .pendingAudits(pendingAudits)
                .activeCompanies(activeCompanies)
                .build();

        return AdminDashboardResponse.builder()
                .totalCompanies(totalCompanies)
                .totalUsers(totalUsers)
                .bestScore(bestScore)
                .worstScore(worstScore)
                .stats(stats)
                .build();
    }
}