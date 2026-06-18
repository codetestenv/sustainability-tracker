package com.sustainabilitytracker.sustainabilitytracker.mappers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.response.ReportResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.EsgReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReportMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "scoreId", source = "score.id")
    @Mapping(target = "downloadUrl", ignore = true)
    ReportResponse toResponse(EsgReport report);

    List<ReportResponse> toResponseList(List<EsgReport> reports);
}