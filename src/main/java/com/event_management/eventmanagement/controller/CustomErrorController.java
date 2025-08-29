package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.utils.ApiMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute("jakarta.servlet.error.status_code");
        Object messageBody = request.getAttribute("jakarta.servlet.error.message");

        ApiMessage message = new ApiMessage(
                status.toString(),
                "Something went wrong",
                messageBody.toString(),
                Collections.emptyMap()
        );

        model.addAttribute("message", message);

        return "error";
    }
}