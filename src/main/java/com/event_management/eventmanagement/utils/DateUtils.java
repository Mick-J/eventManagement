package com.event_management.eventmanagement.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class DateUtils {
    // Check if date is valid (non-empty and correctly formatted)
    public static boolean isDateValid(String dateStr) {
        return parseDate(dateStr, "yyyy-MM-dd").isPresent();
    }

    // Check if dateBegin is before dateEnd
    public static boolean isDateBeginBeforeEnd(String dateBeginStr, String dateEndStr, String patternStr) {
        return parseDate(dateBeginStr, patternStr)
                .flatMap(begin -> parseDate(dateEndStr, patternStr)
                        .map(end -> begin.isBefore(end)))
                .orElse(false);
    }

    // Check if two dates are equal
    public static boolean isDatesEqual(String dateBeginStr, String dateEndStr, String patternStr) {
        return parseDate(dateBeginStr, patternStr)
                .flatMap(begin -> parseDate(dateEndStr, patternStr)
                        .map(end ->begin.isEqual(end)))
                .orElse(false);
    }

    // Parses a date safely using Optional<LocalDate> and given pattern
    private static Optional<LocalDate> parseDate(String dateStr, String patternStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return Optional.empty();
        }
        try {
            String pattern = patternStr.isEmpty() ? "yyyy-MM-dd" : patternStr;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
            return Optional.of(LocalDate.parse(dateStr.strip(), dtf));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

}