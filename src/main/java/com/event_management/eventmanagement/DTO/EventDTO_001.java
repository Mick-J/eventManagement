package com.event_management.eventmanagement.DTO;

import com.event_management.eventmanagement.model.CompanyAttendee;
import com.event_management.eventmanagement.model.LeadEvent;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.List;

public record EventDTO_001(
    Integer id,
    @NotNull(message = "The event type is required") Integer eventTypeId,
    @NotNull(message = "The event company is required") Integer companyId,
    @NotNull(message = "The event responsible is required") Integer eventResponsible,
    @NotEmpty(message = "The event name is required") String eventTitle,
    @NotEmpty(message = "The event location is required") String eventLocation,
    @Size(max = 500) String eventDescription,
    @NotEmpty(message = "Event begin date error") String eventDateBegin,
    @NotEmpty(message = "Event end date error") String eventDateEnd,
    @NotEmpty(message = "Event begin time error") String eventTimeBegin,
    @NotEmpty(message = "Event end time error") String eventTimeEnd,
    List<LeadEvent> leadEventList,
    List<CompanyAttendee> companyAttendeeList,
    @Column(name = "created_at") Instant createdAt,
    @ColumnDefault("CURRENT_TIMESTAMP") Instant updatedAt
) {}