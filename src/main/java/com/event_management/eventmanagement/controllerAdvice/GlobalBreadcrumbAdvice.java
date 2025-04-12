package com.event_management.eventmanagement.controllerAdvice;

import com.event_management.eventmanagement.service.BreadcrumbService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalBreadcrumbAdvice {

    private final BreadcrumbService breadcrumbService;

    public GlobalBreadcrumbAdvice(BreadcrumbService breadcrumbService) {
        this.breadcrumbService = breadcrumbService;
    }

    @ModelAttribute
    public void addBreadcrumbs(Model model, HttpServletRequest request) {
        model.addAttribute("breadcrumbLinks", breadcrumbService.generateBreadcrumbs(request));
    }
}