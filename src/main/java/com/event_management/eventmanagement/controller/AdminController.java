package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.model.User;
import com.event_management.eventmanagement.model.UserListDTO;
import com.event_management.eventmanagement.repository.CompanyRepository;
import com.event_management.eventmanagement.repository.UserCompanyRepository;
import com.event_management.eventmanagement.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepo;
    private final UserCompanyRepository userCompanyRepo;
    private final CompanyRepository companyRepo;

    public AdminController(UserRepository userRepo, UserCompanyRepository userCompanyRepo,
                           CompanyRepository companyRepo) {
        this.userRepo = userRepo;
        this.userCompanyRepo = userCompanyRepo;
        this.companyRepo = companyRepo;
    }

    @GetMapping({"", "/"})
    @PreAuthorize("hasRole('ADMIN')")
    public String getUserList(Model model) {
        //
        List<UserListDTO> userList = userCompanyRepo.findAll().stream().map(
                user -> {
                    UserListDTO userListDTO = new UserListDTO();
                    userListDTO.setId(user.getId());
                    //
                    User userData = userRepo.findById(user.getId()).orElse(null);
                    if (userData != null) {
                        userListDTO.setUserFirstname(userData.getUserFirstname());
                        userListDTO.setUserLastname(userData.getUserLastname());
                        userListDTO.setUserEmail(userData.getUserEmail());
                        userListDTO.setUserPhone(userData.getUserPhone());
                        userListDTO.setUserRole(userData.getUserRole());
                    }
                    //
                    companyRepo.findById(user.getCompany().getId()).ifPresent(company -> userListDTO.setUserCompanyName(company.getCompanyName()));
                    return userListDTO;
                }).collect(Collectors.toList());

        // sort
        List<UserListDTO> sortedUserListDTO = userList.stream()
                .sorted(Comparator.comparing(UserListDTO::getUserFirstname)
                        .thenComparing(UserListDTO::getUserLastname))
                .collect(Collectors.toList());

        model.addAttribute("userList", sortedUserListDTO);
        return "admin/index";
    }
}