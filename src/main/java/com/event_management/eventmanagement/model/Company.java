package com.event_management.eventmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Size(max = 255)
    @Column(name = "website")
    private String website;

    @Size(max = 255)
    @Column(name = "contact_email")
    private String contactEmail;

    @Size(max = 20)
    @Column(name = "company_phone", length = 20)
    private String companyPhone;

    @Size(max = 20)
    @Column(name = "company_fax", length = 20)
    private String companyFax;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    public Company() {
    }

    public Company(String companyName, String website, String contactEmail, String companyPhone, String companyFax, Instant createdAt, Instant updatedAt, Country country) {
        this.companyName = companyName;
        this.website = website;
        this.contactEmail = contactEmail;
        this.companyPhone = companyPhone;
        this.companyFax = companyFax;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.country = country;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", website='" + website + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", companyPhone='" + companyPhone + '\'' +
                ", companyFax='" + companyFax + '\'' +
                ", createdAt=" + createdAt +
//                ", updatedAt=" + updatedAt +
                ", country=" + country +
                '}';
    }
}