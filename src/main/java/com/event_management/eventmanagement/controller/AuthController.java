package com.event_management.eventmanagement.controller;


import com.event_management.eventmanagement.model.User;
import com.event_management.eventmanagement.model.UserDTO;
import com.event_management.eventmanagement.repository.UserRepository;
import com.event_management.eventmanagement.service.UserService;
import com.event_management.eventmanagement.utils.SanitizationUtils;
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
    private final UserRepository userRepo;

    public AuthController(UserService userService, UserRepository userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
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
        model.addAttribute("userDTO", new UserDTO());
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registerUser(Model model, @ModelAttribute UserDTO userDTO) {
        if (userDTO.getUserEmail().isBlank()) { return "redirect:/auth/registration?error=true"; }
        User user = userRepo.findByUserEmail(userDTO.getUserEmail()).orElse(null);
        if (user != null) {
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("error", true);
            return "auth/registration";
        }
        try {
            SanitizationUtils.sanitizeObjectFields(userDTO, "escape");
            userService.saveUser(userDTO);
        } catch (Exception ex) {
            logger.info("Error while unescape html tags in new register user object with email = {}", userDTO.getUserEmail());
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.logout();
        } catch (ServletException e) {
            logger.info("Error when logout {}:", e.getMessage());
        }
        return "redirect:/auth/login?logout";
    }
}