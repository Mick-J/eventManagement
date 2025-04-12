package com.event_management.eventmanagement.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class EventTypeDTO {

    @NotBlank(message = "Event type is required")
    @Size(max = 20, message = "Event type must not exceed 20 characters")
    private String eventType;

    private Instant updatedAt;

    public EventTypeDTO() {
    }

    public EventTypeDTO(String eventType, Instant updatedAt) {
        this.eventType = eventType;
        this.updatedAt = updatedAt;
    }

    public @NotBlank(message = "Event type is required") @Size(max = 20, message = "Event type must not exceed 20 characters") String getEventType() {
        return eventType;
    }

    public void setEventType(@NotBlank(message = "Event type is required") @Size(max = 20, message = "Event type must not exceed 20 characters") String eventType) {
        this.eventType = eventType;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "EventTypeDTO{" +
                "eventType='" + eventType + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
