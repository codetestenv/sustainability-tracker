package com.sustainabilitytracker.sustainabilitytracker.dtos.request;

import com.sustainabilitytracker.sustainabilitytracker.enums.AuditAction;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuditRequest {

    @NotNull(message = "Action is required")
    private AuditAction action;

    private String comments;

    private String flaggedItems;
}