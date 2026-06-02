package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.DepartmentRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.DepartmentResponse;
import com.sustainabilitytracker.sustainabilitytracker.services.DepartmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/departments")
@AllArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/{companyId}")
    public ResponseEntity<List<DepartmentResponse>> getDepartmentByCompany(@Valid @PathVariable Long companyId) {
        List<DepartmentResponse> departments = departmentService.getDepartmentsByCompany(companyId);
        return ResponseEntity.ok(departments);
    }


    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(
            @Valid @RequestBody DepartmentRequest request,
            UriComponentsBuilder uriBuilder) {

        DepartmentResponse departmentResponse = departmentService.createDepartment(request);
        var uri = uriBuilder.path("api/v1/departments/{id}").buildAndExpand(departmentResponse.getId()).toUri();

        return ResponseEntity.created(uri).body(departmentResponse);
    }

    @PutMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @Valid @RequestBody DepartmentRequest request,
            @PathVariable Long departmentId) {
        DepartmentResponse departmentResponse = departmentService.updateDepartment(departmentId,request);
        return ResponseEntity.ok(departmentResponse);
    }
}

//- DELETE /api/v1/departments/{id}
//        → deactivateDepartment()