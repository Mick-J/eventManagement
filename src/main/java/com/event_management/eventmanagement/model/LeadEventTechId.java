package com.event_management.eventmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.util.Objects;

@Embeddable
public class LeadEventTechId implements java.io.Serializable {
    @Serial private static final long serialVersionUID = -8164391304365033422L;
    @NotNull
    @Column(name = "event_id", nullable = false)
    private Integer eventId;

    @NotNull
    @Column(name = "lead_id", nullable = false)
    private Integer leadId;

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LeadEventTechId entity = (LeadEventTechId) o;
        return Objects.equals(this.eventId, entity.eventId) &&
                Objects.equals(this.leadId, entity.leadId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, leadId);
    }

}