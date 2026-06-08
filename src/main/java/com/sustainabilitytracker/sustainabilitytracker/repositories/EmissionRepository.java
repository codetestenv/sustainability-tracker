package com.sustainabilitytracker.sustainabilitytracker.repositories;


import com.sustainabilitytracker.sustainabilitytracker.entities.Company;
import com.sustainabilitytracker.sustainabilitytracker.entities.Department;
import com.sustainabilitytracker.sustainabilitytracker.entities.EmissionData;
import com.sustainabilitytracker.sustainabilitytracker.entities.User;
import com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EmissionRepository extends JpaRepository<EmissionData, Long> {
    boolean existsByDepartmentIdAndRecordedAtAndStatus(Long departmentId, LocalDate attr0, DataStatus status);

    List<EmissionData> findAllByCompany(Company company);

    List<EmissionData> findAllBySubmittedBy(User submittedBy);

    List<EmissionData> findAllByDepartment(Department department);

    List<EmissionData> findAllByCompany_Id(Long companyId);
}
