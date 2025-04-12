package com.event_management.eventmanagement.model;


import jakarta.validation.constraints.NotEmpty;

public class UserDTO {

    private String userFirstname;
    private String userLastname;
    private String userEmail;
    @NotEmpty(message = "Password is required")
    private String userPassword;
    @NotEmpty(message = "Confirm Password is required")
    private String checkPassword;
    private String userPhone;
    private String userRole;
    private String userActive;


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

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserActive() {
        return userActive;
    }

    public void setUserActive(String userActive) {
        this.userActive = userActive;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userFirstname='" + userFirstname + '\'' +
                ", userLastname='" + userLastname + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPhone='" + userPhone + '\'' +
                '}';
    }
}