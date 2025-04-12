package com.event_management.eventmanagement.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.List;

public class EventDTO {

    private Integer id;

    @NotEmpty(message = "The event name is required")
    private String eventTitle;

    @NotNull(message = "The event type is required")
    private Integer eventTypeId;

    @Size(max = 500)
    private String eventDescription;

    @NotEmpty(message = "Event begin date error")
    private String eventDateBegin;

    @NotEmpty(message = "Event end date error")
    private String eventDateEnd;

    @NotEmpty(message = "Event begin time error")
    private String eventTimeBegin;

    @NotEmpty(message = "Event end time error")
    private String eventTimeEnd;

    @NotEmpty(message = "The event location is required")
    private String eventLocation;

    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    private Instant updatedAt;

    @NotNull(message = "The event company is required")
    private Integer companyId;

    @NotNull(message = "The event responsible is required")
    private Integer eventResponsible;

    private List<Integer> leadIds;

    private List<Integer> companyAttendeeIds;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Integer getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId( Integer eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventDateBegin() {
        return eventDateBegin;
    }

    public void setEventDateBegin(String eventDateBegin) {
        this.eventDateBegin = eventDateBegin;
    }

    public String getEventDateEnd() {
        return eventDateEnd;
    }

    public void setEventDateEnd(String eventDateEnd) {
        this.eventDateEnd = eventDateEnd;
    }

    public String getEventTimeBegin() {
        return eventTimeBegin;
    }

    public void setEventTimeBegin(String eventTimeBegin) {
        this.eventTimeBegin = eventTimeBegin;
    }

    public String getEventTimeEnd() {
        return eventTimeEnd;
    }

    public void setEventTimeEnd(String eventTimeEnd) {
        this.eventTimeEnd = eventTimeEnd;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public @NotNull(message = "The event responsible is required") Integer getEventResponsible() {
        return eventResponsible;
    }

    public void setEventResponsible(@NotNull(message = "The event responsible is required") Integer eventResponsible) {
        this.eventResponsible = eventResponsible;
    }

    public List<Integer> getLeadIds() {
        return leadIds;
    }

    public void setLeadIds(List<Integer> leadIds) {
        this.leadIds = leadIds;
    }

    public List<Integer> getCompanyAttendeeIds() {
        return companyAttendeeIds;
    }

    public void setCompanyAttendeeIds(List<Integer> companyAttendeeIds) {
        this.companyAttendeeIds = companyAttendeeIds;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "eventTitle='" + eventTitle + '\'' +
                ", eventTypeId=" + eventTypeId +
                ", eventDescription='" + eventDescription + '\'' +
                ", eventDateBegin='" + eventDateBegin + '\'' +
                ", eventDateEnd='" + eventDateEnd + '\'' +
                ", eventTimeBegin='" + eventTimeBegin + '\'' +
                ", eventTimeEnd='" + eventTimeEnd + '\'' +
                ", eventLocation='" + eventLocation + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", companyId=" + companyId +
                ", eventResponsible=" + eventResponsible +
                ", leadIds=" + leadIds +
                ", companyAttendeeIds=" + companyAttendeeIds +
                '}';
    }
}