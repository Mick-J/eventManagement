package com.event_management.eventmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.util.Objects;

@Embeddable
public class CompanyAttendeeId implements java.io.Serializable {
    @Serial private static final long serialVersionUID = -5010171288899314493L;
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @NotNull
    @Column(name = "event_id", nullable = false)
    private Integer eventId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CompanyAttendeeId entity = (CompanyAttendeeId) o;
        return Objects.equals(this.eventId, entity.eventId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, userId);
    }

}