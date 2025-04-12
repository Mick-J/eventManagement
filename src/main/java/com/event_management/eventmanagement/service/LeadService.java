package com.event_management.eventmanagement.service;

import com.event_management.eventmanagement.model.LeadEvent;
import com.event_management.eventmanagement.repository.LeadEventRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeadService {
    private final LeadEventRepository leadEventRepo;

    public LeadService(LeadEventRepository leadEventRepo) {
        this.leadEventRepo = leadEventRepo;
    }

    public List<LeadEvent> getLeadList() {
        Sort sort = Sort.by(Sort.Order.asc("leadFirstname"), Sort.Order.asc("leadLastname"));
        return leadEventRepo.findAll(sort);
    }

}