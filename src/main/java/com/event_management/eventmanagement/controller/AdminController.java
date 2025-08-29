package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.DTO.UserListDTO;
import com.event_management.eventmanagement.model.Company;
import com.event_management.eventmanagement.model.User;
import com.event_management.eventmanagement.repository.UserCompanyRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserCompanyRepository userCompanyRepo;

    public AdminController(UserCompanyRepository userCompanyRepo) {
        this.userCompanyRepo = userCompanyRepo;
    }

    @GetMapping({"", "/"})
    @PreAuthorize("hasRole('ADMIN')")
    public String getUserList(Model model) {
        //
        List<UserListDTO> userList = userCompanyRepo.findAll().stream()
                .flatMap(userCompany -> {
                    User userData = userCompany.getUser();
                    Company company = userCompany.getCompany();
                    if (userData == null || company == null) { return Stream.empty(); }

                    return Stream.of(new UserListDTO(
                            userData.getId(),
                            userData.getUserFirstname(),
                            userData.getUserLastname(),
                            userData.getUserEmail(),
                            userData.getUserPhone(),
                            userData.getUserActive(),
                            userData.getUserRole(),
                            userData.getCreatedAt(),
                            company.getCompanyName()
                    ));
                })
                .toList();

        // sort
        List<UserListDTO> sortedUserListDTO = userList.stream()
                .sorted(Comparator.comparing(UserListDTO::userFirstname)
                        .thenComparing(UserListDTO::userLastname))
                .collect(Collectors.toList());

        model.addAttribute("userList", sortedUserListDTO);
        return "admin/index";
    }
}