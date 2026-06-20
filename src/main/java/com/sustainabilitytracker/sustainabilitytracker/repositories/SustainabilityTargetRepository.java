package com.sustainabilitytracker.sustainabilitytracker.repositories;

import com.sustainabilitytracker.sustainabilitytracker.entities.SustainabilityTarget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface SustainabilityTargetRepository extends JpaRepository<SustainabilityTarget, Long> {
    Optional<BigDecimal> findTargetValue(Long companyId, String co2Emission, LocalDate start, LocalDate end);
}
