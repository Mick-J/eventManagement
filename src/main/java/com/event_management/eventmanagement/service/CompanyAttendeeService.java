package com.event_management.eventmanagement.service;

import com.event_management.eventmanagement.repository.CompanyAttendeeRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyAttendeeService {

    private final CompanyAttendeeRepository companyAttendeeRepo;

    public CompanyAttendeeService(CompanyAttendeeRepository companyAttendeeRepo) {
        this.companyAttendeeRepo = companyAttendeeRepo;
    }

}