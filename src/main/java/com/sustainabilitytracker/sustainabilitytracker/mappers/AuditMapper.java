package com.sustainabilitytracker.sustainabilitytracker.mappers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.response.AuditResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.AuditRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuditMapper {

    @Mapping(target = "reportId",    source = "report.id")
    @Mapping(target = "reportTitle", source = "report.reportTitle")
    @Mapping(target = "auditorId",   source = "auditor.id")
    @Mapping(target = "auditorName", source = "auditor.fullName")
    @Mapping(target = "createdAt",   source = "createdAt")
    AuditResponse toResponse(AuditRecord auditRecord);

    List<AuditResponse> toResponseList(List<AuditRecord> auditRecords);
}