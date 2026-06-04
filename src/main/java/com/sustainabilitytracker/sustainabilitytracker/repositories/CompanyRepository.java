package com.sustainabilitytracker.sustainabilitytracker.repositories;

import com.sustainabilitytracker.sustainabilitytracker.entities.Company;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByName(String name);
    List<Company> findByIsActiveTrue();
    boolean existsByNameAndIdNot(String name, Long id);
    List<Company> findAllByIsActive(Boolean isActive);

    Optional<Company> findByIdAndIsActiveTrue(Long companyId);
}
