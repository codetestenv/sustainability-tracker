package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.response.CompanyResponse;
import com.sustainabilitytracker.sustainabilitytracker.mappers.CompanyMapper;
import com.sustainabilitytracker.sustainabilitytracker.services.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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