package com.event_management.eventmanagement.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;

public class LeadEventDTO {
    @NotEmpty(message = "Lead first name is required")
    private String leadFirstName;
    @NotEmpty(message = "Lead first name is required")
    private String leadLastName;
    @Email(message = "Lead email should be valid")
    private String leadEmail;
    private String leadPhone;
    private String leadCompanyName;
    private String leadInterestLevel;
    private String comment;
    private String leadState;
    private String leadBCardImg;
    private Instant createdAt;
    private Instant updatedAt;

    public String getLeadFirstName() {
        return leadFirstName;
    }

    public void setLeadFirstName(String leadFirstName) {
        this.leadFirstName = leadFirstName;
    }

    public String getLeadLastName() {
        return leadLastName;
    }

    public void setLeadLastName(String leadLastName) {
        this.leadLastName = leadLastName;
    }

    public String getLeadEmail() {
        return leadEmail;
    }

    public void setLeadEmail(String leadEmail) {
        this.leadEmail = leadEmail;
    }

    public String getLeadPhone() {
        return leadPhone;
    }

    public void setLeadPhone(String leadPhone) {
        this.leadPhone = leadPhone;
    }

    public String getLeadCompanyName() {
        return leadCompanyName;
    }

    public void setLeadCompanyName(String leadCompanyName) {
        this.leadCompanyName = leadCompanyName;
    }

    public String getLeadInterestLevel() {
        return leadInterestLevel;
    }

    public void setLeadInterestLevel(String leadInterestLevel) {
        this.leadInterestLevel = leadInterestLevel;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLeadState() {
        return leadState;
    }

    public void setLeadState(String leadState) {
        this.leadState = leadState;
    }

    public String getLeadBCardImg() {
        return leadBCardImg;
    }

    public void setLeadBCardImg(String leadBCardImg) {
        this.leadBCardImg = leadBCardImg;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "LeadEventDTO{" +
                "leadFirstName='" + leadFirstName + '\'' +
                ", leadLastName='" + leadLastName + '\'' +
                ", leadEmail='" + leadEmail + '\'' +
                ", leadPhone='" + leadPhone + '\'' +
                ", leadCompanyName='" + leadCompanyName + '\'' +
                ", leadInterestLevel='" + leadInterestLevel + '\'' +
                ", comment='" + comment + '\'' +
                ", leadState='" + leadState + '\'' +
                ", leadBCardImg='" + leadBCardImg + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
