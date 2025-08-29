package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.DTO.UserDTO;
import com.event_management.eventmanagement.service.UserService;
import com.event_management.eventmanagement.utils.ApiMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/edit")
    String editUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Optional<UserDTO> userDTOOptional = userService.getUserDTOByEmail(userDetails.getUsername());

        if (userDTOOptional.isEmpty()) {
            logger.warn("User with email {} not found", userDetails.getUsername());
            return "error";
        }
        model.addAttribute("userDTO", userDTOOptional.get());
        return "/user/edit";
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editUser(@AuthenticationPrincipal UserDetails userDetails,
                                      @Valid @ModelAttribute UserDTO userDTO,
                                      HttpServletRequest request,
                                      HttpServletResponse response
    ) {
        return userService.updateUser(userDetails, userDTO, request, response);
    }

    @GetMapping("/display")
    String displayUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        return userService.displayUser(userDetails, model);
    }
}