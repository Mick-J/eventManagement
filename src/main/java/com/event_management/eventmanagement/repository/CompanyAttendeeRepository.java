package com.event_management.eventmanagement.repository;

import com.event_management.eventmanagement.model.CompanyAttendee;
import com.event_management.eventmanagement.model.CompanyAttendeeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyAttendeeRepository extends JpaRepository<CompanyAttendee, CompanyAttendeeId> {
}
