package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.model.LeadEvent;
import com.event_management.eventmanagement.repository.EventRepository;
import com.event_management.eventmanagement.repository.LeadEventRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardRestController {

    private final EventRepository eventRepo;
    private final LeadEventRepository leadEventRepo;

    public DashboardRestController(EventRepository eventRepo, LeadEventRepository leadEventRepo) {
        this.eventRepo = eventRepo;
        this.leadEventRepo = leadEventRepo;
    }

    @GetMapping("/event-by-type")
    public Map<String, Long> getEventsByType() {
        return eventRepo.findAll().stream()
                .collect(Collectors.groupingBy(evt -> evt.getEventType().getEventType(), Collectors.counting()));
    }

    @GetMapping("/event-by-company")
    public Map<String, Long> getEventsByCompany() {
        return eventRepo.findAll().stream()
                .collect(Collectors.groupingBy(evt -> evt.getCompany().getCompanyName(), Collectors.counting()));
    }

    @GetMapping("/lead-by-interest")
    public Map<String, Long> getLeadsByInterestLevel() {
        return leadEventRepo.findAll().stream()
                .collect(Collectors.groupingBy(LeadEvent::getLeadInterestLevel, Collectors.counting()));
    }

    @GetMapping("/lead-by-status")
    public Map<String, Long> getLeadsByStatus() {
        return leadEventRepo.findAll().stream()
                .collect(Collectors.groupingBy(LeadEvent::getLeadState, Collectors.counting()));
    }
}