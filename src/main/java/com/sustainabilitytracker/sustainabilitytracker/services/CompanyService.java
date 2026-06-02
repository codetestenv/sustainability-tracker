package com.sustainabilitytracker.sustainabilitytracker.services;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.CompanyRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.CompanyResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.Company;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.DuplicateResourceException;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.ResourceNotFoundException;
import com.sustainabilitytracker.sustainabilitytracker.mappers.CompanyMapper;
import com.sustainabilitytracker.sustainabilitytracker.repositories.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyResponse createCompany(CompanyRequest request){
        if (companyRepository.existsByName(request.getName()))
            throw new DuplicateResourceException(request.getName() + " is already exist.");

        var company = companyMapper.toEntity(request);
        company.setIsActive(true);
        Company savedCompany = companyRepository.save(company);

        return companyMapper.toResponse(savedCompany);
    }

    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAllByIsActive(true)
                .stream()
                .map(companyMapper::toResponse)
                .toList();
    }

    @Transactional
    public void deactivateCompany(Long companyId) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with id: " + companyId
                        )
                );

        company.setIsActive(false);

        company.getUsers().forEach(user -> user.setIsActive(false));

    }
}