package com.event_management.eventmanagement.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class DateUtils {
    // Check if date is valid (non-empty and correctly formatted)
    public static boolean isDateValid(String dateStr, String patternStr) {
        String pattern = patternStr.isEmpty() ? "yyyy-MM-dd" : patternStr;
        return parseDate(dateStr, pattern).isPresent();
    }

    // Check if dateBegin is before dateEnd
    public static boolean isDateBeginBeforeEnd(String dateBeginStr, String dateEndStr, String patternStr) {
        if(!isDateValid(dateBeginStr, patternStr)) {
            return false;
        }
        if(!isDateValid(dateEndStr, patternStr)) {
            return false;
        }
        return parseDate(dateBeginStr, patternStr)
                .flatMap(begin -> parseDate(dateEndStr, patternStr)
                        .map(begin::isBefore))
                .orElse(false);
    }

    // Check if two dates are equal
    public static boolean isDatesEqual(String dateBeginStr, String dateEndStr, String patternStr) {
        return parseDate(dateBeginStr, patternStr)
                .flatMap(begin -> parseDate(dateEndStr, patternStr)
                        .map(begin::isEqual))
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