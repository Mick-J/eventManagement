package com.event_management.eventmanagement.utils;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

public class SanitizationUtils {
    private static final Logger logger = LogManager.getLogger(SanitizationUtils.class);

    /**
     *
     * @param object
     * @param type : value escape|unescape
     * @param <T>
     */
    public static <T> void sanitizeObjectFields(T object, String type) {
        if (object == null) return;

        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true); // Allow access to private fields
            try {
                if (field.getType().equals(String.class)) {
                    String originalValue = (String) field.get(object);

                    if (originalValue != null) {
                        String sanitizedValue;
                        originalValue = originalValue.strip();
                        if (type.equals("escape")) {
                            sanitizedValue = StringEscapeUtils.escapeHtml4(originalValue);
                        } else {
                            sanitizedValue = StringEscapeUtils.unescapeHtml4(originalValue);
                        }
                        field.set(object, sanitizedValue); // Set sanitized value back
                    }
                }
            } catch (IllegalAccessException e) {
                logger.info( "Sanitization {} error when {}", object.getClass().toString(), type);
                e.printStackTrace();
            }
        }
    }
}