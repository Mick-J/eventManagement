package com.event_management.eventmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "lead_event")
public class LeadEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lead_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "lead_firstname")
    private String leadFirstname;

    @Size(max = 255)
    @Column(name = "lead_lastname")
    private String leadLastname;

    @Size(max = 255)
    @NotNull
    @Column(name = "lead_email", nullable = false)
    private String leadEmail;

    @Size(max = 20)
    @Column(name = "lead_phone", length = 20)
    private String leadPhone;

    @Size(max = 255)
    @Column(name = "lead_company_name")
    private String leadCompanyName;

    @Size(max = 10)
    @ColumnDefault("'MEDIUM'")
    @Column(name = "lead_interest_level", length = 10)
    private String leadInterestLevel;

    @Size(max = 500)
    @Column(name = "comment", length = 500)
    private String comment;

    @Size(max = 10)
    @ColumnDefault("'WAITING'")
    @Column(name = "lead_state", length = 10)
    private String leadState;

    @Size(max = 200)
    @Column(name = "lead_b_card_img", length = 200)
    private String leadBCardImg;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToMany(mappedBy = "leadList")
    private List<Event> EventList;

    public LeadEvent() {
    }

    public LeadEvent(String leadFirstname, String leadLastname, String leadEmail,
                     String leadPhone, String leadCompanyName, String leadInterestLevel,
                     String comment, String leadState, String leadBCardImg, Instant createdAt,
                     Instant updatedAt) {
        this.leadFirstname = leadFirstname;
        this.leadLastname = leadLastname;
        this.leadEmail = leadEmail;
        this.leadPhone = leadPhone;
        this.leadCompanyName = leadCompanyName;
        this.leadInterestLevel = leadInterestLevel;
        this.comment = comment;
        this.leadState = leadState;
        this.leadBCardImg = leadBCardImg;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLeadFirstname() {
        return leadFirstname;
    }

    public void setLeadFirstname(String leadFirstname) {
        this.leadFirstname = leadFirstname;
    }

    public String getLeadLastname() {
        return leadLastname;
    }

    public void setLeadLastname(String leadLastname) {
        this.leadLastname = leadLastname;
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

    public List<Event> getEventList() {
        return EventList;
    }

    public void setEventList(List<Event> eventList) {
        EventList = eventList;
    }

    @Override
    public String toString() {
        return "LeadEvent{" +
                "id=" + id +
                ", leadFirstname='" + leadFirstname + '\'' +
                ", leadLastname='" + leadLastname + '\'' +
                ", leadEmail='" + leadEmail + '\'' +
                ", leadPhone='" + leadPhone + '\'' +
                ", leadCompanyName='" + leadCompanyName + '\'' +
                ", leadInterestLevel='" + leadInterestLevel + '\'' +
                ", comment='" + comment + '\'' +
                ", leadState='" + leadState + '\'' +
                ", leadBCardImg='" + leadBCardImg + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
//                ", EventList=" + EventList +
                '}';
    }
}