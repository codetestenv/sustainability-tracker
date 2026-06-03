package com.sustainabilitytracker.sustainabilitytracker.mappers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.DepartmentRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.DepartmentResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    Department toEntity(DepartmentRequest request);

    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "createdAt", source = "createdAt")
    DepartmentResponse toResponse(Department department);
}