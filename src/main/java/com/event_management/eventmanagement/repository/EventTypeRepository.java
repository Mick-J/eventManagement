package com.event_management.eventmanagement.repository;

import com.event_management.eventmanagement.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Integer> {
}
