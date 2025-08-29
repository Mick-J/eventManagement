package com.event_management.eventmanagement.utils;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ApiMessage implements Serializable {
        @Serial private static final long serialVersionUID = 1L;

        private String messageType;
        private String messageHeader;
        private String messageBody;
        private Map<String, String> detailList = new HashMap<>();

        public ApiMessage() {
        }

        public ApiMessage(String messageType,
                       String messageHeader,
                       String messageBody,
                       Map<String, String> detailList) {
            this.messageType = messageType;
            this.messageHeader = messageHeader;
            this.messageBody = messageBody;
            this.detailList = detailList;
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

    @Override
    public String toString() {
        return "ApiMessage{" +
                "messageType='" + messageType + '\'' +
                ", messageHeader='" + messageHeader + '\'' +
                ", messageBody='" + messageBody + '\'' +
                ", detailList=" + detailList +
                '}';
    }
}