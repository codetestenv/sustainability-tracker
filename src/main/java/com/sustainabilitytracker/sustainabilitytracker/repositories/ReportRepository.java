package com.sustainabilitytracker.sustainabilitytracker.repositories;

import com.sustainabilitytracker.sustainabilitytracker.entities.EsgReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<EsgReport, Long> {

    List<EsgReport> findByCompanyIdOrderByCreatedAtDesc(Long companyId);

    Optional<EsgReport> findByIdAndCompanyId(Long id, Long companyId);
}