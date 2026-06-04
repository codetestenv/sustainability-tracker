package com.sustainabilitytracker.sustainabilitytracker.repositories;


import com.sustainabilitytracker.sustainabilitytracker.entities.EmissionData;
import com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface EmissionRepository extends JpaRepository<EmissionData, Long> {
    boolean existsByDepartmentIdAndRecordedAtAndStatus(Long departmentId, LocalDate attr0, DataStatus status);
}
