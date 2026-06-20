package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.DepartmentRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.DepartmentResponse;
import com.sustainabilitytracker.sustainabilitytracker.services.DepartmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;


    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<DepartmentResponse>> getDepartmentsByCompany(
            @PathVariable Long companyId) {

        List<DepartmentResponse> departments = departmentService.getDepartmentsByCompany(companyId);
        return ResponseEntity.ok(departments);
    }


    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(
            @Valid @RequestBody DepartmentRequest request,
            UriComponentsBuilder uriBuilder) {

        DepartmentResponse departmentResponse = departmentService.createDepartment(request);

        var uri = uriBuilder.path("/api/v1/departments/{id}")
                .buildAndExpand(departmentResponse.getId())
                .toUri();

        return ResponseEntity.created(uri).body(departmentResponse);
    }

    @PutMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Long departmentId,
            @Valid @RequestBody DepartmentRequest request) {

        DepartmentResponse response = departmentService.updateDepartment(departmentId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> deactivateDepartment(@PathVariable Long departmentId) {
        departmentService.deactivateDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }
}