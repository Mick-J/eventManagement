package com.event_management.eventmanagement.service;

import com.event_management.eventmanagement.model.Company;
import com.event_management.eventmanagement.repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private final CompanyRepository companyRepo;

    public CompanyService(CompanyRepository companyRepo) {
        this.companyRepo = companyRepo;
    }

    public int getLastCompanyId() {
        Company company = companyRepo.findTopByOrderByIdDesc();
        return (company != null) ? company.getId(): 1;
    }
}