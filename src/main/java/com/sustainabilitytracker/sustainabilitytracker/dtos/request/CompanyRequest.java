package com.sustainabilitytracker.sustainabilitytracker.dtos.request;

import com.sustainabilitytracker.sustainabilitytracker.enums.CompanySize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompanyRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String industry;

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotNull(message = "Company size is required")
    private CompanySize size;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

}
