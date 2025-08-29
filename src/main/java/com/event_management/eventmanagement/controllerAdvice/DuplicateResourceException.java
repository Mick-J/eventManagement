package com.event_management.eventmanagement.controllerAdvice;

public class DuplicateResourceException extends RuntimeException {

    private final String userMessage;

    public DuplicateResourceException(String logMessage, String userMessage) {
        super(logMessage);
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
