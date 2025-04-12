package com.event_management.eventmanagement.utils;

public enum LeadState {
    PENDING,
    PROCESSED,
    IGNORE;

    public String print() {
        String name = name().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
