package com.event_management.eventmanagement.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;

public record LeadEventDTO (
    Integer id,
    @NotBlank(message = "Lead first name is required")
    String leadFirstName,
    @NotBlank(message = "Lead first name is required")
    String leadLastName,
    @Email(message = "Lead email should be valid")
    String leadEmail,
    String leadPhone,
    String leadCompanyName,
    String leadInterestLevel,
    String comment,
    String leadState,
    String leadBCardImg,
    Instant createdAt,
    Instant updatedAt
) {}