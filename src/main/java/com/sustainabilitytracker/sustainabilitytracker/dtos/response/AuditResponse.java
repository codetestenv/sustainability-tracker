package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import com.sustainabilitytracker.sustainabilitytracker.enums.AuditAction;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AuditResponse {

    private Long id;
    private Long reportId;
    private String reportTitle;
    private Long auditorId;
    private String auditorName;
    private AuditAction action;
    private String comments;
    private String flaggedItems;
    private Instant reviewedAt;
}
