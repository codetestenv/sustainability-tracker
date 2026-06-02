package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.CompanyRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.CompanyResponse;
import com.sustainabilitytracker.sustainabilitytracker.mappers.CompanyMapper;
import com.sustainabilitytracker.sustainabilitytracker.services.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    @GetMapping
    public List<CompanyResponse> getAllCompanies(){
       return companyService.getAllCompanies();
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(
            @RequestBody CompanyRequest request,
            UriComponentsBuilder uriBuilder){

        var companyResponse = companyService.createCompany(request);
        var uri = uriBuilder.path("api/v1/company/{id}").buildAndExpand(companyResponse.getId()).toUri();

       return ResponseEntity.created(uri).body(companyResponse);
    }
}
//         → getAllCompanies()
//- POST   /api/v1/companies
//         → createCompany()
//- GET    /api/v1/companies/{id}
//        → getCompanyById()
//- PUT    /api/v1/companies/{id}
//        → updateCompany()
//- DELETE /api/v1/companies/{id}
//        → deactivateCompany()