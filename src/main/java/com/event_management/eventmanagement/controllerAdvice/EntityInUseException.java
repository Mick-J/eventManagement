package com.event_management.eventmanagement.controllerAdvice;

public class EntityInUseException extends RuntimeException {
    private final String userMessage;
    private final String redirectPath;

    public EntityInUseException(String logMessage, String userMessage, String redirectPath) {
        super(logMessage);
        this.userMessage = userMessage;
        this.redirectPath = redirectPath;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getRedirectPath() {
        return redirectPath;
    }
}