package com.sustainabilitytracker.sustainabilitytracker.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RejectEmissionRequest {

    @NotBlank(message = "Reason is required")
    private String reason;
}