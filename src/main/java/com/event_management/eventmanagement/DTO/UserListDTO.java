package com.event_management.eventmanagement.DTO;

import java.time.Instant;

public record UserListDTO (
    Integer id,
    String userFirstname,
    String userLastname,
    String userEmail,
    String userPhone,
    String userActive,
    String userRole,
    Instant createdAt,
    String userCompanyName

) {}
