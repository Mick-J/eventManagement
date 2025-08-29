package com.event_management.eventmanagement.service;

import com.event_management.eventmanagement.controllerAdvice.ResourceNotFoundException;
import com.event_management.eventmanagement.model.User;
import com.event_management.eventmanagement.DTO.UserDTO;
import com.event_management.eventmanagement.repository.UserRepository;
import com.event_management.eventmanagement.utils.ApiMessage;
import com.event_management.eventmanagement.utils.SanitizationUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email" + email));

        return new org.springframework.security.core.userdetails.User(
                user.getUserEmail(),
                user.getUserPassword(),
                user.getUserActive().equalsIgnoreCase("Y"),
                true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserRole()))
        );
    }

    public String registerUser(UserDTO userDTO, Model model) {
        if (userDTO.userEmail().isBlank()) { return "redirect:/auth/registration?error=true"; }
        User user = userRepo.findByUserEmail(userDTO.userEmail()).orElse(null);
        if (user != null) {
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("error", true);
            return "auth/registration";
        }
        try {
            SanitizationUtils.sanitizeObjectFields(userDTO, "escape");
            userRepo.save(mapToUser(userDTO, null, "create"));
        } catch (Exception ex) {
            logger.info("Error while unescape html tags in new register user object with email = {}",
                    userDTO.userEmail());
        }
        return "redirect:/auth/login";
    }

    public ResponseEntity<?> updateUser(UserDetails userDetails, UserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {
        User user = userRepo.findByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with email: " + userDTO.userEmail() + " not found when updating user",
                        "User not found"));
        //
        if (isPasswordValid(userDTO)) {
            return ResponseEntity.badRequest().body(new ApiMessage(
                    "error",
                    "Error on password",
                    "Password should not be empty",
                    Collections.emptyMap()
            ));
        }
        //
        if (!isBothPasswordEqual(userDTO)) {
            return ResponseEntity.badRequest().body(new ApiMessage(
                    "error",
                    "Error on password",
                    "Both password should be the same",
                    Collections.emptyMap()
            ));
        }

        //
        user = mapToUser(userDTO, user, "update");

        //
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        //
        SanitizationUtils.sanitizeObjectFields(user, "escape");
        userRepo.save(user);
        return ResponseEntity.ok(new ApiMessage(
                "success",
                "Update success",
                "User updated successfully",
                Collections.emptyMap()
        ));
    }

    private boolean isPasswordValid(UserDTO userDTO) {
        return userDTO.userPassword().isBlank() && userDTO.checkPassword().isBlank();
    }

    private boolean isBothPasswordEqual(UserDTO userDTO) {
        return userDTO.userPassword().equals(userDTO.checkPassword());
    }

    public String displayUser(UserDetails userDetails, Model model) {
        User user = userRepo.findByUserEmail(userDetails.getUsername()).orElse(null);
        if (user == null) {
            logger.warn("User with email: {} not found", userDetails.getUsername());
            return "error";
        }
        user.setUserPassword("*".repeat(10));
        try {
            SanitizationUtils.sanitizeObjectFields(user, "unescape");
        } catch (Exception e) {
            logger.info("Error while unescape html tags in user object with id = {}", user.getId());
        }
        model.addAttribute("user", user);
        return "/user/display";
    }


    public User mapToUser(UserDTO userDTO, User user, String type) {

        // create
        if (type.equals("create")) {
            user = new User();
            user.setUserRole("USER");
            user.setUserActive("Y");
            user.setCreatedAt(Instant.now());
            user.setUpdatedAt(Instant.now());
            user.setUserEmail(userDTO.userEmail());
        }
        // update
        if (type.equals("update")) {
            if (userDTO.userRole() != null && !userDTO.userRole().isBlank()) {
                user.setUserRole(userDTO.userRole());
            }
            if (userDTO.userActive() != null && !userDTO.userActive().isBlank()) {
                user.setUserActive(userDTO.userActive());
            }
        }
        //
        user.setUserFirstname(userDTO.userFirstname());
        user.setUserLastname(userDTO.userLastname());
        user.setUserPassword(passwordEncoder.encode(userDTO.userPassword()));
        user.setUserPhone(userDTO.userPhone());
        user.setUpdatedAt(Instant.now());

        return user;
    }

    public void addModelUserList(Model model) {
        Sort sort = Sort.by(Sort.Order.asc("userFirstname"), Sort.Order.asc("userLastname"));
        model.addAttribute("userList", userRepo.findAll(sort));
    }

    public Optional<UserDTO> getUserDTOByEmail(String username) {
        return userRepo.findByUserEmail(username).map((user)->{
            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getUserFirstname(),
                    user.getUserLastname(),
                    user.getUserEmail(),
                    "",
                    "",
                    user.getUserPhone(),
                    user.getUserRole(),
                    user.getUserActive()
            );
            //
            SanitizationUtils.sanitizeObjectFields(userDTO, "unescape");
            return userDTO;
        });
    }


}