package com.sustainabilitytracker.sustainabilitytracker.services;

import com.sustainabilitytracker.sustainabilitytracker.dtos.response.AdminDashboardResponse;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.DashboardResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.Company;
import com.sustainabilitytracker.sustainabilitytracker.entities.SustainabilityScore;
import com.sustainabilitytracker.sustainabilitytracker.entities.User;
import com.sustainabilitytracker.sustainabilitytracker.enums.AuditStatus;
import com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus;
import com.sustainabilitytracker.sustainabilitytracker.enums.Role;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.AccessDeniedException;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.ResourceNotFoundException;
import com.sustainabilitytracker.sustainabilitytracker.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final SocialRepository socialRepository;
    private final AuthService authService;

    public DashboardResponse getCompanyDashboard(Long companyId) {

        User currentUser = authService.getCurrentUser();

        if (!hasAccessToCompany(currentUser, companyId)) {
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

        // Pending Items
        int pendingApprovals = getPendingApprovalsCount(companyId);
        int pendingReports = getPendingReportsCount(companyId);
        int unreadNotifications = 5; // TODO: Integrate real notification count later

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
                .unreadNotifications(unreadNotifications)
                .build();
    }

    public AdminDashboardResponse getAdminDashboard() {

        User currentUser = authService.getCurrentUser();

        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only admin can access admin dashboard");
        }

        Long totalCompanies = companyRepository.count();
        Long totalUsers = userRepository.count();

        SustainabilityScore bestScore = scoreRepository.findTopByOrderByTotalScoreDesc().orElse(null);
        SustainabilityScore worstScore = scoreRepository.findTopByOrderByTotalScoreAsc().orElse(null);

        return AdminDashboardResponse.builder()
                .totalCompanies(totalCompanies)
                .totalUsers(totalUsers)
                .bestScore(bestScore)
                .worstScore(worstScore)
                .systemStats(Map.of(
                        "totalReports", reportRepository.count(),
                        "pendingAudits", reportRepository.countByAuditStatus(AuditStatus.PENDING)
                ))
                .build();
    }


    private boolean hasAccessToCompany(User user, Long companyId) {
        if (user.getRole() == Role.ADMIN || user.getRole() == Role.AUDITOR) return true;
        return user.getCompany() != null && user.getCompany().getId().equals(companyId);
    }

    private int getPendingApprovalsCount(Long companyId) {
        int pendingEmission = (int) emissionRepository.countByCompanyIdAndStatus(companyId, DataStatus.PENDING);
        int pendingEnergy = (int) energyRepository.countByCompanyIdAndStatus(companyId, DataStatus.PENDING);
        int pendingWater = (int) waterRepository.countByCompanyIdAndStatus(companyId, DataStatus.PENDING);
        int pendingWaste = (int) wasteRepository.countByCompanyIdAndStatus(companyId, DataStatus.PENDING);
        int pendingSocial = (int) socialRepository.countByCompanyIdAndStatus(companyId, DataStatus.PENDING);

        return pendingEmission + pendingEnergy + pendingWater + pendingWaste + pendingSocial;
    }

    private int getPendingReportsCount(Long companyId) {
        return (int) reportRepository.countByCompanyIdAndAuditStatus(companyId, AuditStatus.PENDING);
    }
}
