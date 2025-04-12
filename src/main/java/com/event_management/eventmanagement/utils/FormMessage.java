package com.event_management.eventmanagement.utils;

import java.util.Map;

public class FormMessage {
    public static Message withMessage(Map<String, String> detailsList, String ...params) {
            return new Message.Builder()
                    .withMessageType(params[0])
                    .withMessageHeader(params[1])
                    .withMessageBody(params[2])
                    .withDetailList(detailsList)
                    .build();
    }
}