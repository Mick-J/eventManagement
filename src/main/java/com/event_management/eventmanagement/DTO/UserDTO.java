package com.event_management.eventmanagement.DTO;


import jakarta.validation.constraints.NotEmpty;

public record UserDTO (
    Integer id,
    String userFirstname,
    String userLastname,
    String userEmail,
    @NotEmpty(message = "Password is required") String userPassword,
    @NotEmpty(message = "Confirm Password is required") String checkPassword,
    String userPhone,
    String userRole,
    String userActive) {

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