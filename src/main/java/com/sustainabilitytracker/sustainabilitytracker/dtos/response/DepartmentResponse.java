package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import lombok.Data;

import java.time.Instant;

@Data
public class DepartmentResponse {
    private Long id;
    private String name;
    private String description;
    private String companyName;
    private Boolean isActive;
    private Instant createdAt;
}
