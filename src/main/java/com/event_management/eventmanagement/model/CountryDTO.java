package com.event_management.eventmanagement.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class CountryDTO {

    @NotEmpty(message = "Country name is required")
    private String countryName;
    @NotEmpty(message = "Country code is required")
    private String countryCode;
    @Pattern(regexp = "\\d+", message = "Must contain only digits")
    @NotEmpty(message = "Country dial code is required")
    private String countryDialCode;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryDialCode() {
        return countryDialCode;
    }

    public void setCountryDialCode(String countryDialCode) {
        this.countryDialCode = countryDialCode;
    }

    @Override
    public String toString() {
        return "CountryDTO{" +
                "countryName='" + countryName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", countryDialCode='" + countryDialCode + '\'' +
                '}';
    }
}
