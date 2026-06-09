package com.sustainabilitytracker.sustainabilitytracker.repositories;

import com.sustainabilitytracker.sustainabilitytracker.entities.EnergyData;
import com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus;
import com.sustainabilitytracker.sustainabilitytracker.projection.EnergyTotalsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EnergyRepository
        extends JpaRepository<EnergyData, Long> {

    List<EnergyData> findByCompanyId(Long companyId);

    List<EnergyData> findByDepartmentId(Long departmentId);

    List<EnergyData> findByCompanyIdAndStatus(
            Long companyId,
            DataStatus status
    );

    // Employee sees his own submissions
    List<EnergyData> findBySubmittedBy_Id(Long userId);

    List<EnergyData> findByCompanyIdAndRecordedAtBetween(
            Long companyId,
            LocalDate start,
            LocalDate end
    );

    @Query("""
            SELECT
                SUM(e.totalKwh)      AS totalKwh,
                SUM(e.renewableKwh)  AS totalRenewableKwh,
                AVG(e.totalKwh)      AS averageKwh,
                COUNT(e.id)          AS recordCount
            FROM EnergyData e
            WHERE e.company.id = :companyId
            AND e.recordedAt BETWEEN :start AND :end
            AND e.status = 'APPROVED'
            """)
    EnergyTotalsProjection getTotalsByCompanyAndPeriod(
            @Param("companyId") Long companyId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );
}
