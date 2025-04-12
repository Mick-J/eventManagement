package com.event_management.eventmanagement.repository;

import com.event_management.eventmanagement.model.LeadEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadEventRepository extends JpaRepository<LeadEvent, Integer> {
    // Get the latest LeadEvent by ID
    LeadEvent findTopByOrderByIdDesc();
}
