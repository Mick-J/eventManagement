package com.event_management.eventmanagement.service;

import com.event_management.eventmanagement.model.User;
import com.event_management.eventmanagement.model.UserDTO;
import com.event_management.eventmanagement.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
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
        System.out.println(user);
        return new org.springframework.security.core.userdetails.User(
                user.getUserEmail(),
                user.getUserPassword(),
                user.getUserActive().equals("Y"),
                true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserRole()))
        );
    }

    public User saveUser(UserDTO userDTO) {
        User user = new User();
        user.setUserFirstname(userDTO.getUserFirstname());
        user.setUserLastname(userDTO.getUserLastname());
        user.setUserEmail(userDTO.getUserEmail());
        user.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));
        user.setUserRole("USER");
        user.setUserActive("Y");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        return userRepo.save(user);
    }

    public User updateUser(UserDTO userDTO, User user) {
        user.setUserFirstname(userDTO.getUserFirstname());
        user.setUserLastname(userDTO.getUserLastname());
        user.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));
        user.setUserPhone(userDTO.getUserPhone());

        if (userDTO.getUserRole() != null && !userDTO.getUserRole().isBlank()) {
            user.setUserRole(userDTO.getUserRole());
        }
        if (userDTO.getUserActive() != null && !userDTO.getUserActive().isBlank()) {
            user.setUserActive(userDTO.getUserActive());
        }
        user.setUpdatedAt(Instant.now());

        return userRepo.save(user);
    }
}