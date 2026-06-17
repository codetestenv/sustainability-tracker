package com.sustainabilitytracker.sustainabilitytracker.services;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.ReportRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.ReportResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.Company;
import com.sustainabilitytracker.sustainabilitytracker.entities.EsgReport;
import com.sustainabilitytracker.sustainabilitytracker.entities.SustainabilityScore;
import com.sustainabilitytracker.sustainabilitytracker.entities.User;
import com.sustainabilitytracker.sustainabilitytracker.enums.AuditStatus;
import com.sustainabilitytracker.sustainabilitytracker.enums.ReportType;
import com.sustainabilitytracker.sustainabilitytracker.enums.Role;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.AccessDeniedException;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.ResourceNotFoundException;
import com.sustainabilitytracker.sustainabilitytracker.mappers.ReportMapper;
import com.sustainabilitytracker.sustainabilitytracker.repositories.CompanyRepository;
import com.sustainabilitytracker.sustainabilitytracker.repositories.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final CompanyRepository companyRepository;
    private final AuthService authService;
    private final ScoreCalculationService scoreCalculationService;
    private final ReportMapper reportMapper;

    private final String reportStoragePath = "uploads/reports/";

    @Transactional
    public ReportResponse generateReport(ReportRequest request, Long currentUserId) {

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + request.getCompanyId()));

        User currentUser = authService.getCurrentUser();

        if (!hasAccessToCompany(currentUser, company.getId())) {
            throw new AccessDeniedException("You do not have access to generate report for this company");
        }

        LocalDate start = request.getPeriodStart();
        LocalDate end = request.getPeriodEnd();

        // Calculate score
        SustainabilityScore score = scoreCalculationService.calculateAndSaveScore(company.getId(), start, end);

        // Generate file
        String fileName = generateFileName(company, start, end, request.getFileFormat());
        String filePath = reportStoragePath + fileName;

        byte[] fileBytes = generateReportFile(company, start, end, request.getFileFormat());
        saveFileToStorage(fileBytes, filePath);

        // Save report
        EsgReport report = EsgReport.builder()
                .company(company)
                .score(score)
                .generatedBy(currentUser)
                .reportTitle(request.getReportTitle() != null ? request.getReportTitle() : "ESG Report")
                .reportType(request.getReportType() != null ? request.getReportType() : ReportType.FULL_ESG)
                .filePath(filePath)
                .fileFormat(request.getFileFormat() != null ? request.getFileFormat() : "PDF")
                .periodStart(start)
                .periodEnd(end)
                .auditStatus(AuditStatus.PENDING)
                .build();

        EsgReport savedReport = reportRepository.save(report);

        return reportMapper.toResponse(savedReport);
    }

    public List<ReportResponse> getReportsByCompany(Long companyId) {
        User currentUser = authService.getCurrentUser();

        if (!hasAccessToCompany(currentUser, companyId)) {
            throw new AccessDeniedException("You do not have access to this company's reports");
        }

        List<EsgReport> reports = reportRepository.findByCompanyIdOrderByCreatedAtDesc(companyId);

        return reportMapper.toResponseList(reports);
    }

    public byte[] downloadReport(Long reportId) {
        EsgReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));

        User currentUser = authService.getCurrentUser();

        if (!hasAccessToCompany(currentUser, report.getCompany().getId())) {
            throw new AccessDeniedException("You do not have access to this report");
        }

        return readFileFromStorage(report.getFilePath());
    }


    private boolean hasAccessToCompany(User user, Long companyId) {
        if (user.getRole() == Role.ADMIN || user.getRole() == Role.AUDITOR) return true;
        return user.getCompany() != null && user.getCompany().getId().equals(companyId);
    }

    private String generateFileName(Company company, LocalDate start, LocalDate end, String format) {
        String ext = format != null ? format.toLowerCase() : "pdf";
        return company.getName().replace(" ", "_") +
                "_ESG_Report_" + start + "_to_" + end + "." + ext;
    }

    private byte[] generateReportFile(Company company, LocalDate start, LocalDate end, String format) {
        // Implement real PDF/Excel generation (Apache POI or iText)
        String content = "ESG Sustainability Report\nCompany: " + company.getName() +
                "\nPeriod: " + start + " to " + end;
        return content.getBytes();
    }

    private void saveFileToStorage(byte[] bytes, String filePath) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            Files.write(file.toPath(), bytes);
        } catch (IOException e) {
            log.error("Failed to save report file", e);
            throw new RuntimeException("Failed to save report file");
        }
    }

    private byte[] readFileFromStorage(String filePath) {
        try {
            return Files.readAllBytes(new File(filePath).toPath());
        } catch (IOException e) {
            log.error("Failed to read report file", e);
            throw new RuntimeException("Failed to read report file");
        }
    }
}
