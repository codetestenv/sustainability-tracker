package com.sustainabilitytracker.sustainabilitytracker.repositories;

import com.sustainabilitytracker.sustainabilitytracker.entities.WaterData;
import com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus;
import com.sustainabilitytracker.sustainabilitytracker.projection.WaterTotalsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface WaterRepository extends JpaRepository<WaterData, Long> {

    boolean existsByDepartmentIdAndRecordedAtAndStatus(
            Long departmentId, LocalDate recordedAt, DataStatus status);

    List<WaterData> findByCompanyId(Long companyId);
    List<WaterData> findBySubmittedBy_Id(Long userId);
    List<WaterData> findByDepartmentId(Long departmentId);

    // For summary (custom projection)
    @Query("SELECT " +
            "SUM(w.consumedLiters) as totalConsumedLiters, " +
            "SUM(w.recycledLiters) as totalRecycledLiters, " +
            "COUNT(w.id) as recordCount, " +
            "COALESCE(SUM(w.recycledLiters) * 100.0 / NULLIF(SUM(w.consumedLiters), 0), 0) as recyclingRate " +
            "FROM WaterData w " +
            "WHERE w.company.id = :companyId " +
            "AND w.status = 'APPROVED' " +
            "AND w.recordedAt BETWEEN :start AND :end")
    WaterTotalsProjection getTotalsByCompanyAndPeriod(
            @Param("companyId") Long companyId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

    BigDecimal getTotalConsumedByCompanyAndPeriod(Long companyId, LocalDate start, LocalDate end);

    BigDecimal getTotalRecycledByCompanyAndPeriod(Long companyId, LocalDate start, LocalDate end);

    BigDecimal getTotalRecycledLiters(Long companyId, LocalDate start, LocalDate end);

    BigDecimal getTotalConsumedLiters(Long companyId, LocalDate start, LocalDate end);
}
