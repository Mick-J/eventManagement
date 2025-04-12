package com.event_management.eventmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "user_firstname", nullable = false)
    private String userFirstname;

    @Size(max = 255)
    @NotNull
    @Column(name = "user_lastname", nullable = false)
    private String userLastname;

    @Size(max = 255)
    @NotNull
    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Size(min = 8)
    @NotNull
    @Column(name = "user_email", unique = true, nullable = false)
    private String userEmail;

    @Size(max = 20)
    @Column(name = "user_phone", length = 20)
    private String userPhone;

    @Size(max = 1)
    @ColumnDefault("'Y'")
    @Column(name = "user_active", length = 1)
    private String userActive;

    @Size(max = 6)
    @ColumnDefault("'USER'")
    @Column(name = "user_role", length = 6)
    private String userRole;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToMany(mappedBy = "companyAttendeeList")
    private List<Event> EventList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserFirstname() {
        return userFirstname;
    }

    public void setUserFirstname(String userFirstname) {
        this.userFirstname = userFirstname;
    }

    public String getUserLastname() {
        return userLastname;
    }

    public void setUserLastname(String userLastname) {
        this.userLastname = userLastname;
    }

    public @Size(min = 8) @NotNull String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(@Size(max = 255) @NotNull String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserActive() {
        return userActive;
    }

    public void setUserActive(String userActive) {
        this.userActive = userActive;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
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
        return "User{" +
                "id=" + id +
                ", userFirstname='" + userFirstname + '\'' +
                ", userLastname='" + userLastname + '\'' +
//                ", userPasswordHash='" + userPasswordHash + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userActive='" + userActive + '\'' +
                ", userRole='" + userRole + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
//                ", EventList=" + EventList +
                '}';
    }
}