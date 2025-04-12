package com.event_management.eventmanagement.model;

import java.time.Instant;

public class UserListDTO {

    private Integer id;
    private String userFirstname;
    private String userLastname;
    private String userEmail;
    private String userPhone;
    private String userActive;
    private String userRole;
    private Instant createdAt;
    private String userCompanyName;

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

    public String getUserCompanyName() {
        return userCompanyName;
    }

    public void setUserCompanyName(String userCompanyName) {
        this.userCompanyName = userCompanyName;
    }

    @Override
    public String toString() {
        return "UserListDTO{" +
                "id=" + id +
                ", userFirstname='" + userFirstname + '\'' +
                ", userLastname='" + userLastname + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userActive='" + userActive + '\'' +
                ", userRole='" + userRole + '\'' +
                ", createdAt=" + createdAt +
                ", userCompanyName=" + userCompanyName +
                '}';
    }
}
