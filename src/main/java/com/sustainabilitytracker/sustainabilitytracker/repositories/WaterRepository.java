package com.sustainabilitytracker.sustainabilitytracker.repositories;

import com.sustainabilitytracker.sustainabilitytracker.entities.WaterData;
import com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus;
import com.sustainabilitytracker.sustainabilitytracker.projection.WaterTotalsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface WaterRepository extends JpaRepository<WaterData, Long> {

    boolean existsByDepartmentIdAndRecordedAtAndStatus(
            Long departmentId,
            LocalDate recordedAt,
            DataStatus status
    );

    List<WaterData> findByCompanyId(Long companyId);
    List<WaterData> findBySubmittedBy_Id(Long userId);
    List<WaterData> findByDepartmentId(Long departmentId);

    @Query("""
            SELECT
                SUM(w.consumedLiters)   AS totalConsumedLiters,
                SUM(w.recycledLiters)   AS totalRecycledLiters,
                COUNT(w.id)             AS recordCount,
                COALESCE(SUM(w.recycledLiters) * 100.0
                    / NULLIF(SUM(w.consumedLiters), 0), 0) AS recyclingRate
            FROM WaterData w
            WHERE w.company.id = :companyId
            AND w.status = 'APPROVED'
            AND w.recordedAt BETWEEN :start AND :end
            """)
    WaterTotalsProjection getTotalsByCompanyAndPeriod(
            @Param("companyId") Long companyId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
            SELECT COALESCE(SUM(w.consumedLiters), 0)
            FROM WaterData w
            WHERE w.company.id = :companyId
            AND w.recordedAt BETWEEN :start AND :end
            AND w.status = 'APPROVED'
            """)
    BigDecimal getTotalConsumedLiters(
            @Param("companyId") Long companyId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
            SELECT COALESCE(SUM(w.recycledLiters), 0)
            FROM WaterData w
            WHERE w.company.id = :companyId
            AND w.recordedAt BETWEEN :start AND :end
            AND w.status = 'APPROVED'
            """)
    BigDecimal getTotalRecycledLiters(
            @Param("companyId") Long companyId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
            SELECT COUNT(w)
            FROM WaterData w
            WHERE w.company.id = :companyId
            AND w.status = :status
            """)
    int countByCompanyIdAndStatus(
            @Param("companyId") Long companyId,
            @Param("status") DataStatus status
    );
}