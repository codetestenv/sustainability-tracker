package com.sustainabilitytracker.sustainabilitytracker.repositories;


import com.sustainabilitytracker.sustainabilitytracker.entities.Company;
import com.sustainabilitytracker.sustainabilitytracker.entities.Department;
import com.sustainabilitytracker.sustainabilitytracker.entities.EmissionData;
import com.sustainabilitytracker.sustainabilitytracker.entities.User;
import com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus;
import com.sustainabilitytracker.sustainabilitytracker.projection.EmissionTotalsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmissionRepository extends JpaRepository<EmissionData, Long> {

    boolean existsByDepartmentIdAndRecordedAtAndStatus(
            Long departmentId,
            LocalDate recordedAt,
            DataStatus status
    );

    List<EmissionData> findAllByCompany(Company company);
    List<EmissionData> findAllBySubmittedBy(User submittedBy);
    List<EmissionData> findAllByDepartment(Department department);
    List<EmissionData> findAllByCompany_Id(Long companyId);

    List<EmissionData> findAllByCompanyAndStatusAndSubmittedAtBetween(
            Company company,
            DataStatus status,
            Instant startDate,
            Instant endDate
    );

    @Query("""
            SELECT
                SUM(e.co2Amount)  AS totalCO2,
                SUM(e.ch4Amount)  AS totalCH4,
                SUM(e.n2oAmount)  AS totalN2O,
                COUNT(e.id)       AS recordCount
            FROM EmissionData e
            WHERE e.company.id = :companyId
            AND e.recordedAt BETWEEN :start AND :end
            AND e.status = 'APPROVED'
            """)
    EmissionTotalsProjection getTotalsByCompanyAndPeriod(
            @Param("companyId") Long companyId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
    SELECT COALESCE(SUM(e.co2Amount), 0)
    FROM EmissionData e
    WHERE e.company.id = :companyId
      AND e.recordedAt BETWEEN :start AND :end
      AND e.status = 'APPROVED'
    """)
    BigDecimal getTotalCo2(
            @Param("companyId") Long companyId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
            SELECT COUNT(e)
            FROM EmissionData e
            WHERE e.company.id = :companyId
            AND e.status = :status
            """)
    int countByCompanyIdAndStatus(@Param("companyId") Long companyId,@Param("status") DataStatus status);
}

