package com.sustainabilitytracker.sustainabilitytracker.mappers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.DepartmentRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.DepartmentResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentMapper {

    @Mapping(target = "company",   ignore = true)
    @Mapping(target = "users",     ignore = true)
    @Mapping(target = "isActive",  ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Department toEntity(DepartmentRequest request);

    @Mapping(target = "companyName", source = "company.name")
    DepartmentResponse toResponse(Department department);

    List<DepartmentResponse> toResponseList(
            List<Department> departments);
}