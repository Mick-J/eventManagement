package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.model.User;
import com.event_management.eventmanagement.model.UserDTO;
import com.event_management.eventmanagement.repository.UserRepository;
import com.event_management.eventmanagement.service.UserService;
import com.event_management.eventmanagement.utils.FormMessage;
import com.event_management.eventmanagement.utils.Message;
import com.event_management.eventmanagement.utils.SanitizationUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserRepository userRepo;
    private final UserService userService;


    public UserController(UserRepository userRepo, UserService userService) {
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @GetMapping("/edit")
    String editUser(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepo.findByUserEmail(userDetails.getUsername()).orElse(null);
        if (user == null) {
            logger.info("User not found");
            return "error";
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUserFirstname(user.getUserFirstname());
        userDTO.setUserLastname(user.getUserLastname());
        userDTO.setUserPassword("");
        userDTO.setCheckPassword("");
        userDTO.setUserEmail(user.getUserEmail());
        userDTO.setUserPhone(user.getUserPhone());
        //
        try {
            SanitizationUtils.sanitizeObjectFields(userDTO, "unescape");
        } catch (Exception ex) {
            logger.info("Error while sanitizing {} ", ex.getMessage());
        }
        model.addAttribute("userDTO", userDTO);
        return "user/edit";
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editUser(@AuthenticationPrincipal UserDetails userDetails,
                                      @Valid @ModelAttribute UserDTO userDTO,
                                      BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response
    ) {
        String objectName = "User";

        userDTO.setUserEmail(userDetails.getUsername());

        if (userDTO.getUserPassword().isBlank()) {
            result.addError(new FieldError("userDTO", "userPassword", "Error on password"));
        }
        if (userDTO.getCheckPassword().isBlank()) {
            result.addError(new FieldError("userDTO", "checkPassword", "Error on confirm password"));
        }
        if (!userDTO.getUserPassword().equals(userDTO.getCheckPassword())) {
            result.addError(new FieldError("userDTO", "userPassword", "Both password should be the same"));
            result.addError(new FieldError("userDTO", "checkPassword", ""));
        }

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            Message message = FormMessage.withMessage(errors, "error", "Something went wrong",
                    objectName + " not updated");
            return ResponseEntity.badRequest().body(message);
        }

        //
        User user = userRepo.findByUserEmail(userDetails.getUsername()).orElse(null);
        if (user == null) {
            logger.info("Error User is null while find by email: {}", userDetails.getUsername());
            Message message = FormMessage.withMessage(null, "error", "Something went wrong",
                    objectName + " to update not found");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }

        try {
            userService.updateUser(userDTO, user);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            request.getSession().invalidate();
            SecurityContextHolder.clearContext();

            Message message = FormMessage.withMessage(null, "success", objectName + " update",
                    objectName + " updated successfully!");
            return ResponseEntity.ok(message);

        } catch (DataAccessException ex) {
            Throwable rootCause = ex.getRootCause();
            String errorMessage = (rootCause != null) ? rootCause.getMessage() : "Unknown SQL Error";
            logger.error("SQL Error: {}", errorMessage);
        } catch (Exception ex) {
            logger.info("\nError while updating user with email: {}", userDetails.getUsername());
        }

        Message message = FormMessage.withMessage(null, "error", "Something went wrong",
                objectName + " not updated");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @GetMapping("/display")
    String displayUser(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepo.findByUserEmail(userDetails.getUsername()).orElse(null);
        if (user == null) {
            logger.info("User not found");
            return "error";
        }
        user.setUserPassword("*".repeat(10));
        try {
            SanitizationUtils.sanitizeObjectFields(user, "unescape");
        } catch (Exception e) {
            logger.info("Error while unescape html tags in user object with id = {}", user.getId());
        }
        model.addAttribute("user", user);
        return "user/display";
    }
}