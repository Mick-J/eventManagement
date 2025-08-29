package com.event_management.eventmanagement.controller;


import com.event_management.eventmanagement.DTO.UserDTO;
import com.event_management.eventmanagement.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registerUser(Model model) {
        model.addAttribute("userDTO",
                new UserDTO(null,
                        "", "", "", "", "", "",
                        "User", "Y"));
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute UserDTO userDTO, Model model) {
        return userService.registerUser(userDTO, model);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        try {
            request.getSession().invalidate();
            request.logout();
        } catch (ServletException e) {
            logger.info("Error when logout {}:", e.getMessage());
        }
        return "redirect:/auth/login?logout";
    }
}