package com.event_management.eventmanagement.service;

import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class BreadcrumbService {

    public List<Map<String, String>> generateBreadcrumbs(HttpServletRequest request) {
        List<Map<String, String>> breadcrumbs = new ArrayList<>();

        String uri = request.getRequestURI();
        String[] segments = uri.split("/");
        StringBuilder urlPath = new StringBuilder();

        // Add "Home" as the first breadcrumb
        breadcrumbs.add(Map.of("name", "Home", "url", "/"));

        for (String segment : segments) {
            if (!segment.isEmpty() && !isNumeric(segment)) {  // Ignore numeric segments
                urlPath.append("/").append(segment);
                breadcrumbs.add(Map.of("name", capitalize(segment), "url", urlPath.toString()));
            }
        }

        return breadcrumbs;
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+"); // Returns true if the string contains only digits
    }
}