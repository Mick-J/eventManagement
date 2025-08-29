package com.event_management.eventmanagement.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record EventTypeDTO (
    Integer id,
    @NotBlank(message = "Event type is required")
    @Size(max = 20, message = "Event type must not exceed 20 characters")
    String eventType,
    Instant updatedAt
) {}
