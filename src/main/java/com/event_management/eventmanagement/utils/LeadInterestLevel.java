package com.event_management.eventmanagement.utils;

public enum LeadInterestLevel {
    HIGH,
    MEDIUM,
    LOW;

    public String print() {
        String name = name().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
