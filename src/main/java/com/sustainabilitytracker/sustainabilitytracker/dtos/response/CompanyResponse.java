package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import lombok.Data;

import java.time.Instant;

@Data
public class CompanyResponse {
    private Long id;
    private String name;
    private String industry;
    private String country;
    private String city;
    private String size;
    private String email;
    private String phone;
    private Boolean isActive;
    private Instant createdAt;

}