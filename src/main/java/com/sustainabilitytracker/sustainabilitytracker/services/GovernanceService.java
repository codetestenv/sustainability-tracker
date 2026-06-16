package com.sustainabilitytracker.sustainabilitytracker.services;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.GovernanceRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.GovernanceResponse;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.GovernanceSummaryResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.Company;
import com.sustainabilitytracker.sustainabilitytracker.entities.GovernanceData;
import com.sustainabilitytracker.sustainabilitytracker.entities.User;
import com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus;
import com.sustainabilitytracker.sustainabilitytracker.enums.Role;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.*;
import com.sustainabilitytracker.sustainabilitytracker.mappers.GovernanceMapper;
import com.sustainabilitytracker.sustainabilitytracker.projection.GovernanceTotalsProjection;
import com.sustainabilitytracker.sustainabilitytracker.repositories.CompanyRepository;
import com.sustainabilitytracker.sustainabilitytracker.repositories.GovernanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GovernanceService {

    private final GovernanceRepository governanceRepository;
    private final CompanyRepository companyRepository;
    private final AuthService authService;
    private final GovernanceMapper governanceMapper;
//    private final NotificationService notificationService;
//    private final AuditLogService auditLogService;
//    private final ScoreService scoreService;

    @Transactional
    public GovernanceResponse submitGovernance(GovernanceRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + request.getCompanyId()));

        if (!company.getIsActive()) {
            throw new BadRequestException("Company is not active");
        }

        User currentUser = authService.getCurrentUser();

        if (currentUser.getRole() != Role.SUSTAINABILITY_MANAGER) {
            throw new AccessDeniedException("Only Sustainability Manager can submit governance data");
        }

        boolean alreadyApproved = governanceRepository
                .existsByCompanyIdAndRecordedAtAndStatus(company.getId(), request.getRecordedAt(), DataStatus.APPROVED);

        if (alreadyApproved) {
            throw new DuplicateResourceException("Governance data already submitted and approved for this date");
        }

        GovernanceData data = governanceMapper.toEntity(request);
        data.setCompany(company);
        data.setSubmittedBy(currentUser);
        data.setStatus(DataStatus.DRAFT);

        GovernanceData saved = governanceRepository.save(data);

//        notificationService.notifyByRole("SUSTAINABILITY_MANAGER", "New governance data submitted");
//        auditLogService.log("SUBMIT_GOVERNANCE", "GOVERNANCE", saved.getId(), null, saved);

        return governanceMapper.toResponse(saved);
    }

    @Transactional
    public GovernanceResponse submitForApproval(Long governanceId) {
        GovernanceData data = governanceRepository.findById(governanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Governance record not found with id: " + governanceId));

        User currentUser = authService.getCurrentUser();

        if (!data.getSubmittedBy().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only submit your own records for approval");
        }

        if (data.getStatus() != DataStatus.DRAFT) {
            throw new BadRequestException("Only DRAFT records can be submitted for approval");
        }

        data.setStatus(DataStatus.PENDING);
        data.setSubmittedAt(Instant.now());

        GovernanceData updated = governanceRepository.save(data);
//        notificationService.notifyByRole("SUSTAINABILITY_MANAGER", "Governance record waiting for approval");

        return governanceMapper.toResponse(updated);
    }

    @Transactional
    public GovernanceResponse approveGovernance(Long governanceId) {
        GovernanceData data = governanceRepository.findById(governanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Governance record not found with id: " + governanceId));

        User approver = authService.getCurrentUser();
        checkApprovePermission(approver, data);

        if (data.getStatus() != DataStatus.PENDING) {
            throw new BadRequestException("Only PENDING records can be approved");
        }

        data.setStatus(DataStatus.APPROVED);
        data.setApprovedBy(approver);
        data.setApprovedAt(Instant.now());

        GovernanceData updated = governanceRepository.save(data);
//        scoreService.recalculateForCompany(updated.getCompany().getId());
//        notificationService.notifyUser(updated.getSubmittedBy().getId(), "Your governance record has been APPROVED");

        return governanceMapper.toResponse(updated);
    }

    @Transactional
    public GovernanceResponse rejectGovernance(Long governanceId, String reason) {
        GovernanceData data = governanceRepository.findById(governanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Governance record not found with id: " + governanceId));

        User approver = authService.getCurrentUser();
        checkApprovePermission(approver, data);

        if (data.getStatus() != DataStatus.PENDING) {
            throw new BadRequestException("Only PENDING records can be rejected");
        }

        data.setStatus(DataStatus.REJECTED);
        data.setRejectionReason(reason);

        GovernanceData updated = governanceRepository.save(data);
//        notificationService.notifyUser(updated.getSubmittedBy().getId(), "Your governance record has been REJECTED. Reason: " + reason);

        return governanceMapper.toResponse(updated);
    }

    public List<GovernanceResponse> getGovernanceByCompany(Long companyId) {
        User currentUser = authService.getCurrentUser();

        if (!hasCompanyAccess(currentUser, companyId)) {
            throw new AccessDeniedException("You do not have access to this company's governance data");
        }

        List<GovernanceData> list = governanceRepository.findByCompanyId(companyId);

        return governanceMapper.toResponseList(list);
    }

    public GovernanceSummaryResponse getGovernanceSummary(Long companyId, LocalDate start, LocalDate end) {
        GovernanceTotalsProjection totals = governanceRepository
                .getTotalsByCompanyAndPeriod(companyId, start, end);

        return GovernanceSummaryResponse.builder()
                .recordCount(totals.getRecordCount().intValue())
                .period(start + " to " + end)
                .build();
    }

    private void checkApprovePermission(User user, GovernanceData data) {
        switch (user.getRole()) {
            case SUSTAINABILITY_MANAGER:
            case ADMIN:
                return;
            default:
                throw new UnauthorizedException("You do not have permission to approve this record");
        }
    }

    private boolean hasCompanyAccess(User user, Long companyId) {
        if (user.getRole() == Role.ADMIN || user.getRole() == Role.AUDITOR) return true;
        return user.getCompany() != null && user.getCompany().getId().equals(companyId);
    }
}
