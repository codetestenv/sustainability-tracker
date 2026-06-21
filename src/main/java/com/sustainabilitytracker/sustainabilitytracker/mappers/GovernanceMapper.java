package com.sustainabilitytracker.sustainabilitytracker.mappers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.GovernanceRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.GovernanceResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.GovernanceData;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GovernanceMapper {

    @Mapping(target = "company",         ignore = true)
    @Mapping(target = "submittedBy",     ignore = true)
    @Mapping(target = "approvedBy",      ignore = true)
    @Mapping(target = "status",          ignore = true)
    @Mapping(target = "submittedAt",     ignore = true)
    @Mapping(target = "approvedAt",      ignore = true)
    @Mapping(target = "createdAt",       ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    GovernanceData toEntity(GovernanceRequest request);

    @Mapping(target = "companyId",       source = "company.id")
    @Mapping(target = "companyName",     source = "company.name")
    @Mapping(target = "submittedByName", source = "submittedBy.fullName")
    @Mapping(target = "approvedByName",  source = "approvedBy.fullName")
    GovernanceResponse toResponse(GovernanceData governanceData);

    List<GovernanceResponse> toResponseList(
            List<GovernanceData> governanceDataList);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget GovernanceData governanceData,
                      GovernanceRequest request);
}