package com.event_management.eventmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "event_tech")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "event_title", nullable = false)
    private String eventTitle;

    @Size(max = 500)
    @Column(name = "event_description", length = 500)
    private String eventDescription;

    @Size(max = 10)
    @NotNull
    @Column(name = "event_date_begin", nullable = false, length = 8)
    private String eventDateBegin;

    @Size(max = 10)
    @NotNull
    @Column(name = "event_date_end", nullable = false, length = 8)
    private String eventDateEnd;

    @Size(max = 8)
    @NotNull
    @Column(name = "event_time_begin", nullable = false, length = 8)
    private String eventTimeBegin;

    @Size(max = 8)
    @NotNull
    @Column(name = "event_time_end", nullable = false, length = 8)
    private String eventTimeEnd;

    @Size(max = 255)
    @NotNull
    @Column(name = "event_location", nullable = false)
    private String eventLocation;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventType eventType;

    @ManyToOne
    @JoinColumn(name = "event_responsible_id", nullable = false)
    private User eventResponsible;

    @ManyToMany
    @JoinTable(
            name = "lead_event_tech",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "lead_id")
    )
    private List<LeadEvent> leadList;

    @ManyToMany
    @JoinTable(
            name = "company_attendee",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> companyAttendeeList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventDateBegin() {
        return eventDateBegin;
    }

    public void setEventDateBegin(String eventDateBegin) {
        this.eventDateBegin = eventDateBegin;
    }

    public String getEventDateEnd() {
        return eventDateEnd;
    }

    public void setEventDateEnd(String eventDateEnd) {
        this.eventDateEnd = eventDateEnd;
    }

    public String getEventTimeBegin() {
        return eventTimeBegin;
    }

    public void setEventTimeBegin(String eventTimeBegin) {
        this.eventTimeBegin = eventTimeBegin;
    }

    public String getEventTimeEnd() {
        return eventTimeEnd;
    }

    public void setEventTimeEnd(String eventTimeEnd) {
        this.eventTimeEnd = eventTimeEnd;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getEventResponsible() {
        return eventResponsible;
    }

    public void setEventResponsible(User eventResponsible) {
        this.eventResponsible = eventResponsible;
    }

    public List<User> getCompanyAttendeeList() {
        return companyAttendeeList;
    }

    public void setCompanyAttendeeList(List<User> companyAttendeeList) {
        this.companyAttendeeList = companyAttendeeList;
    }

    public List<LeadEvent> getLeadList() {
        return leadList;
    }

    public void setLeadList(List<LeadEvent> leadList) {
        this.leadList = leadList;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", \n eventTitle='" + eventTitle + '\'' +
                ", \n eventDescription='" + eventDescription + '\'' +
                ", \n eventDateBegin='" + eventDateBegin + '\'' +
                ", \n eventDateEnd='" + eventDateEnd + '\'' +
                ", \n eventTimeBegin='" + eventTimeBegin + '\'' +
                ", \n eventTimeEnd='" + eventTimeEnd + '\'' +
                ", \n eventLocation='" + eventLocation + '\'' +
                ", \n company=" + company +
                ", \n eventType=" + eventType +
                ", \n eventResponsible=" + eventResponsible +
                ", \n companyAttendeeList=" + companyAttendeeList +
                ", \n leadList=" + leadList +
                '}';
    }
}