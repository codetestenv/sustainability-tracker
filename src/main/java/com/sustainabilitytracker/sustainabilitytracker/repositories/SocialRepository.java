package com.sustainabilitytracker.sustainabilitytracker.repositories;

import com.sustainabilitytracker.sustainabilitytracker.entities.SocialData;
import com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus;
import com.sustainabilitytracker.sustainabilitytracker.projection.SocialTotalsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SocialRepository extends JpaRepository<SocialData, Long> {

    boolean existsByDepartmentIdAndRecordedAtAndStatus(Long departmentId, LocalDate recordedAt, DataStatus status);

    List<SocialData> findByCompanyId(Long companyId);
    List<SocialData> findBySubmittedBy_Id(Long userId);
    List<SocialData> findByDepartmentId(Long departmentId);

    @Query("SELECT COUNT(s.id) as recordCount FROM SocialData s " +
            "WHERE s.company.id = :companyId AND s.status = 'APPROVED' " +
            "AND s.recordedAt BETWEEN :start AND :end")
    SocialTotalsProjection getTotalsByCompanyAndPeriod(
            @Param("companyId") Long companyId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

    int getTotalSafetyIncidents(Long companyId, LocalDate start, LocalDate end);

    double getAverageFemaleRatio(Long companyId, LocalDate start, LocalDate end);

    BigDecimal getAverageTrainingHours(Long companyId, LocalDate start, LocalDate end);

    BigDecimal getAverageSatisfactionScore(Long companyId, LocalDate start, LocalDate end);

    Object countByCompanyIdAndStatus(Long companyId, DataStatus dataStatus);
}