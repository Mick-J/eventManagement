package com.event_management.eventmanagement.utils;


import java.util.HashMap;
import java.util.Map;

public class Message {
    private final String messageType;
    private final String messageHeader;
    private final String messageBody;
    private Map<String, String> detailList = new HashMap<>();

    // Private constructor to enforce the use of Builder
    private Message(Builder builder) {
        this.messageType = builder.messageType;
        this.messageHeader = builder.messageHeader;
        this.messageBody = builder.messageBody;
        this.detailList = builder.detailList;
    }

    // Static nested Builder class
    public static class Builder {
        private String messageType;
        private String messageHeader;
        private String messageBody;
        private Map<String, String> detailList = new HashMap<>();

        public Builder withMessageType(String messageType) {
            this.messageType = messageType;
            return this;
        }

        public Builder withMessageHeader(String messageHeader) {
            this.messageHeader = messageHeader;
            return this;
        }

        public Builder withMessageBody(String messageBody) {
            this.messageBody = messageBody;
            return this;
        }

        public Builder withDetailList(Map<String, String> detailList) {
            this.detailList = detailList;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public Map<String, String> getDetailList() { return detailList; }
}