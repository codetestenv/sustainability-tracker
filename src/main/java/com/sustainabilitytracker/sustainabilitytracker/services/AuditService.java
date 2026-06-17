package com.sustainabilitytracker.sustainabilitytracker.services;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.AuditRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.AuditResponse;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.ReportResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.AuditRecord;
import com.sustainabilitytracker.sustainabilitytracker.entities.EsgReport;
import com.sustainabilitytracker.sustainabilitytracker.entities.User;
import com.sustainabilitytracker.sustainabilitytracker.enums.AuditAction;
import com.sustainabilitytracker.sustainabilitytracker.enums.AuditStatus;
import com.sustainabilitytracker.sustainabilitytracker.enums.Role;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.AccessDeniedException;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.ResourceNotFoundException;
import com.sustainabilitytracker.sustainabilitytracker.mappers.AuditMapper;
import com.sustainabilitytracker.sustainabilitytracker.repositories.AuditRepository;
import com.sustainabilitytracker.sustainabilitytracker.repositories.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final ReportRepository reportRepository;
    private final AuditRepository auditRepository;
    private final AuthService authService;
//    private final NotificationService notificationService;
    private final AuditMapper auditMapper;

    public List<ReportResponse> getReportsForAudit() {
        User auditor = authService.getCurrentUser();

        if (auditor.getRole() != Role.AUDITOR) {
            throw new AccessDeniedException("Only auditors can access pending reports for review");
        }

        List<EsgReport> reports = reportRepository.findByAuditStatus(AuditStatus.PENDING);
        return reports.stream()
                .map(this::toReportResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AuditResponse reviewReport(Long reportId, AuditRequest request) {

        EsgReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));

        User auditor = authService.getCurrentUser();

        if (auditor.getRole() != Role.AUDITOR) {
            throw new AccessDeniedException("Only auditors can review reports");
        }

        AuditRecord auditRecord = AuditRecord.builder()
                .report(report)
                .auditor(auditor)
                .company(report.getCompany())
                .action(request.getAction())
                .comments(request.getComments())
                .flaggedItems(request.getFlaggedItems())
                .build();

        AuditRecord savedAudit = auditRepository.save(auditRecord);

        // Update report audit status
        report.setAuditStatus(getUpdatedAuditStatus(request.getAction()));
        reportRepository.save(report);

        // Notifications
//        notificationService.notifyUser(
//                report.getCompany().getSustainabilityManagerId(),
//                "Report has been reviewed: " + request.getAction()
//        );

        if (request.getAction() == AuditAction.FLAGGED) {
//            notificationService.notifyAdmins("A report has been flagged for further review");
        }

        if (request.getAction() == AuditAction.REQUESTED_INFO) {
//            notificationService.notifyUser(
//                    report.getGeneratedBy().getId(),
//                    "Additional information requested for your report"
//            );
        }

        return auditMapper.toResponse(savedAudit);
    }

    public List<AuditResponse> getAuditHistory(Long reportId) {
        User currentUser = authService.getCurrentUser();

        EsgReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        if (!hasAccessToReport(currentUser, report)) {
            throw new AccessDeniedException("You do not have access to this report's audit history");
        }

        List<AuditRecord> audits = auditRepository.findByReportIdOrderByCreatedAtDesc(reportId);

        return auditMapper.toResponseList(audits);
    }

    private AuditStatus getUpdatedAuditStatus(AuditAction action) {
        return switch (action) {
            case VERIFIED -> AuditStatus.VERIFIED;
            case FLAGGED -> AuditStatus.FLAGGED;
            case REJECTED -> AuditStatus.REJECTED;
            case REQUESTED_INFO -> AuditStatus.UNDER_REVIEW;
        };
    }

    private boolean hasAccessToReport(User user, EsgReport report) {
        if (user.getRole() == Role.ADMIN || user.getRole() == Role.AUDITOR) return true;
        return user.getCompany() != null &&
                user.getCompany().getId().equals(report.getCompany().getId());
    }

    private ReportResponse toReportResponse(EsgReport report) {
        return ReportResponse.builder()
                .id(report.getId())
                .companyId(report.getCompany().getId())
                .companyName(report.getCompany().getName())
                .periodStart(report.getPeriodStart())
                .periodEnd(report.getPeriodEnd())
                .build();
    }
}