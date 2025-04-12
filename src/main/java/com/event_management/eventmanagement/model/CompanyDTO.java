package com.event_management.eventmanagement.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CompanyDTO {

    @NotEmpty(message = "Company name is required")
    private String companyName;

    private String website;

    @Email(message = "Company email should be valid")
    private String contactEmail;

    @NotEmpty(message = "Company Phone is required")
    private String companyPhone;

    private String companyFax;

    @NotNull(message = "Country of the company is required")
    private int countryId;


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyFax() {
        return companyFax;
    }

    public void setCompanyFax(String companyFax) {
        this.companyFax = companyFax;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return "CompanyDTO{" +
                "companyName='" + companyName + '\'' +
                ", website='" + website + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", companyPhone='" + companyPhone + '\'' +
                ", companyFax='" + companyFax + '\'' +
                ", countryId=" + countryId +
                '}';
    }
}
