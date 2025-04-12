package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"", "/"})
    public String home() {
        return "index";
    }
    
}