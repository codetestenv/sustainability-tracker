package com.sustainabilitytracker.sustainabilitytracker.mappers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.CompanyRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.CompanyResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.Company;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {
    Company toEntity(CompanyRequest companyRequest);

    CompanyResponse toResponse(Company company);
    void update(CompanyRequest companyRequest, @MappingTarget Company company);
}
