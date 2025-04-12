package com.event_management.eventmanagement.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "lead_event_tech")
public class LeadEventTech {
    @EmbeddedId
    private LeadEventTechId id;

    @MapsId("eventId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @MapsId("leadId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "lead_id", nullable = false)
    private LeadEvent lead;


    public LeadEventTech() {
    }

    public LeadEventTech( LeadEvent lead, Event event) {
        this.event = event;
        this.lead = lead;
    }

    public LeadEventTechId getId() {
        return id;
    }

    public void setId(LeadEventTechId id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LeadEvent getLead() {
        return lead;
    }

    public void setLead(LeadEvent lead) {
        this.lead = lead;
    }

}