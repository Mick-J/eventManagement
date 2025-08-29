package com.event_management.eventmanagement.controllerAdvice;

public class ResourceNotFoundException extends RuntimeException {

    private final String userMessage;

    public ResourceNotFoundException(String logMessage, String userMessage) {
        super(logMessage);
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
