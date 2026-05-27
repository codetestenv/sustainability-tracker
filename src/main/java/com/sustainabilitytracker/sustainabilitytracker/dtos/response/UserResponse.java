package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import lombok.Data;
import java.time.Instant;


@Data
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String role;
    private String companyName;
    private String departmentName;
    private Boolean isActive;
    private Instant createdAt;

}
