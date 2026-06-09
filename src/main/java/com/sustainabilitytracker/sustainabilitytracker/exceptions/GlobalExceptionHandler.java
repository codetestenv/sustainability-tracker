package com.sustainabilitytracker.sustainabilitytracker.exceptions;

import com.sustainabilitytracker.sustainabilitytracker.dtos.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorDto> handleDuplicateResource(DuplicateResourceException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorDto(ex.getMessage())
        );
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        errorDto.setStatus(404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> handleBadRequest(BadRequestException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        errorDto.setStatus(400);
        return ResponseEntity.badRequest().body(errorDto);
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDto> handleBusinessException(BusinessException ex){
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        errorDto.setStatus(400);
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        errorDto.setStatus(403);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
    }


//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorDto> handleValidationError(MethodArgumentNotValidException ex) {
//
//        Map<String, String> errors = new HashMap<>();
//
//        ex.getBindingResult().getFieldErrors().forEach(error -> {
//            errors.put(error.getField(), error.getDefaultMessage());
//        });
//
//        String message = "Validation failed: " + String.join(", ", errors.values());
//
//        ErrorDto errorDto = new ErrorDto(message);
//        errorDto.setStatus(400);
//
//        return ResponseEntity.badRequest().body(errorDto);
//    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleInvalidJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorDto("Invalid JSON format in request body.")
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDto> handleUnauthorizedException(Exception ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        errorDto.setStatus(401);
        return  ResponseEntity.status(401).body(errorDto);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGeneralException(Exception ex) {
        ErrorDto errorDto = new ErrorDto("An unexpected error occurred: " + ex.getMessage());
        errorDto.setStatus(500);
        return ResponseEntity.internalServerError().body(errorDto);
    }
}