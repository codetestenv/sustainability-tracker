package com.sustainabilitytracker.sustainabilitytracker.repositories;

import com.sustainabilitytracker.sustainabilitytracker.entities.WasteData;
import com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus;
import com.sustainabilitytracker.sustainabilitytracker.projection.WasteTotalsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface WasteRepository extends JpaRepository<WasteData, Long> {

    boolean existsByDepartmentIdAndRecordedAtAndStatus(
            Long departmentId, LocalDate recordedAt, DataStatus status);

    List<WasteData> findByCompanyId(Long companyId);
    List<WasteData> findBySubmittedBy_Id(Long userId);
    List<WasteData> findByDepartmentId(Long departmentId);


    @Query("SELECT " +
            "SUM(w.totalKg) as totalKg, " +
            "SUM(w.recycledKg) as totalRecycledKg, " +
            "SUM(w.hazardousKg) as totalHazardousKg, " +
            "COUNT(w.id) as recordCount, " +
            "COALESCE(SUM(w.recycledKg) * 100.0 / NULLIF(SUM(w.totalKg), 0), 0) as recyclingRate " +
            "FROM WasteData w " +
            "WHERE w.company.id = :companyId " +
            "AND w.status = 'APPROVED' " +
            "AND w.recordedAt BETWEEN :start AND :end")
    WasteTotalsProjection getTotalsByCompanyAndPeriod(
            @Param("companyId") Long companyId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

    BigDecimal getTotalKgByCompanyAndPeriod(Long companyId, LocalDate start, LocalDate end);

    BigDecimal getTotalRecycledKgByCompanyAndPeriod(Long companyId, LocalDate start, LocalDate end);

    BigDecimal getTotalKg(Long companyId, LocalDate start, LocalDate end);

    BigDecimal getTotalRecycledKg(Long companyId, LocalDate start, LocalDate end);
}