package com.sustainabilitytracker.sustainabilitytracker.mappers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.EnergyRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.EnergyResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.EnergyData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EnergyMapper {

    @Mapping(target = "id",              ignore = true)
    @Mapping(target = "company",         ignore = true)
    @Mapping(target = "department",      ignore = true)
    @Mapping(target = "submittedBy",     ignore = true)
    @Mapping(target = "approvedBy",      ignore = true)
    @Mapping(target = "status",          ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "submittedAt",     ignore = true)
    @Mapping(target = "approvedAt",      ignore = true)
    @Mapping(target = "createdAt",       ignore = true)
    EnergyData toEntity(EnergyRequest request);

    @Mapping(target = "companyName",     source = "company.name")
    @Mapping(target = "departmentName",  source = "department.name")
    @Mapping(target = "submittedByName", source = "submittedBy.fullName")
    @Mapping(target = "approvedByName",  source = "approvedBy.fullName")
    EnergyResponse toResponse(EnergyData energyData);

    List<EnergyResponse> toResponseList(List<EnergyData> energyDataList);
}