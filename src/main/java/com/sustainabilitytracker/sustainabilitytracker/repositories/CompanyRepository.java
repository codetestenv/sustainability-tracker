package com.sustainabilitytracker.sustainabilitytracker.repositories;

import com.sustainabilitytracker.sustainabilitytracker.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByName(String name);
    List<Company> findByIsActiveTrue();
    boolean existsByNameAndIdNot(String name, Long id);
    List<Company> findAllByIsActive(Boolean isActive);

    Optional<Company> findByIdAndIsActiveTrue(Long companyId);
    boolean existsByIdAndIsActiveTrue(Long companyId);

    @Query("SELECT c.co2Target FROM Company c WHERE c.id = :id")
    BigDecimal getCo2Target(@Param("id") Long id);
}
