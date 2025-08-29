package com.event_management.eventmanagement.repository;

import com.event_management.eventmanagement.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    boolean existsByEventTypeId(Integer eventTypeId);

    boolean existsByCompanyId(Integer companyId);

    Event findTopByOrderByIdDesc();
}
