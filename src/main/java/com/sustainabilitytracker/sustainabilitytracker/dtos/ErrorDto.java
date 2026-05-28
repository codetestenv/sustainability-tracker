package com.sustainabilitytracker.sustainabilitytracker.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ErrorDto {

    private LocalDateTime timestamp = LocalDateTime.now();
    private int status = 400;
    private String message;

    public ErrorDto(String message) {
        this.message = message;
    }

    public ErrorDto(int status, String message) {
        this.status = status;
        this.message = message;
    }
}