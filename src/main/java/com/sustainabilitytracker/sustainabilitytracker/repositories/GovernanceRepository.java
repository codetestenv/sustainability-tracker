package com.sustainabilitytracker.sustainabilitytracker.repositories;

import com.sustainabilitytracker.sustainabilitytracker.entities.GovernanceData;
import com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus;
import com.sustainabilitytracker.sustainabilitytracker.projection.GovernanceTotalsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GovernanceRepository extends JpaRepository<GovernanceData, Long> {

    boolean existsByCompanyIdAndRecordedAtAndStatus(Long companyId, LocalDate recordedAt, DataStatus status);

    List<GovernanceData> findByCompanyId(Long companyId);

    @Query("SELECT COUNT(g.id) as recordCount, " +
            "AVG(g.complianceScore) as averageComplianceScore, " +
            "SUM(g.policyCount) as totalPolicies, " +
            "SUM(g.violationsCount) as totalViolations, " +
            "AVG(g.boardDiversityPct) as averageBoardDiversity " +
            "FROM GovernanceData g " +
            "WHERE g.company.id = :companyId AND g.status = 'APPROVED' " +
            "AND g.recordedAt BETWEEN :start AND :end")
    GovernanceTotalsProjection getTotalsByCompanyAndPeriod(
            @Param("companyId") Long companyId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);
}