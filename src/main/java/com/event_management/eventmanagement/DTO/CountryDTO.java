package com.event_management.eventmanagement.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record CountryDTO (
    Integer id,
    @NotEmpty(message = "Country name is required") String countryName,
    @NotEmpty(message = "Country code is required") String countryCode,
    @Pattern(regexp = "\\d+", message = "Must contain only digits")
    @NotEmpty(message = "Country dial code is required")  String countryDialCode
){}
