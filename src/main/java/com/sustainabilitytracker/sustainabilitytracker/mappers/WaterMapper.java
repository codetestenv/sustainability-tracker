package com.sustainabilitytracker.sustainabilitytracker.mappers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.WaterRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.WaterResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.WaterData;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WaterMapper {

    @Mapping(target = "company", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "submittedBy", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    WaterData toEntity(WaterRequest request);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "submittedByName", source = "submittedBy.fullName")
    @Mapping(target = "approvedByName", source = "approvedBy.fullName")
    WaterResponse toResponse(WaterData waterData);

    List<WaterResponse> toResponseList(List<WaterData> waterDataList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget WaterData waterData, WaterRequest request);
}
