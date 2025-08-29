package com.event_management.eventmanagement.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CompanyDTO (
    Integer id,
    @NotEmpty(message = "Company name is required") String companyName,
     String website,
    @Email(message = "Company email should be valid") String contactEmail,
    @NotEmpty(message = "Company Phone is required") String companyPhone,
    String companyFax,
    @NotNull(message = "Country of the company is required") int countryId
){};