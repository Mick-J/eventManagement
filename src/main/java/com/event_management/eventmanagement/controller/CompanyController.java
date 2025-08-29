package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.DTO.CompanyDTO;
import com.event_management.eventmanagement.controllerAdvice.ResourceNotFoundException;
import com.event_management.eventmanagement.model.Company;
import com.event_management.eventmanagement.model.Country;
import com.event_management.eventmanagement.repository.CompanyRepository;
import com.event_management.eventmanagement.repository.CountryRepository;
import com.event_management.eventmanagement.service.CompanyService;
import com.event_management.eventmanagement.service.CountryService;
import com.event_management.eventmanagement.utils.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/company")
public class CompanyController {

    private final CompanyRepository companyRepo;
    private final CountryRepository countryRepo;
    private final CompanyService companyService;
    private final CountryService countyService;

    public CompanyController(CompanyRepository companyRepo,
                             CountryRepository countryRepo,
                             CountryService countyService,
                             CompanyService companyService) {
        this.companyRepo = companyRepo;
        this.countryRepo = countryRepo;
        this.countyService = countyService;
        this.companyService = companyService;
    }

    @GetMapping({"", "/"})
    public String getCompanyList(Model model) {
        Sort sort = Sort.by(Sort.Direction.ASC, "companyName");
        List<Company> companyList = companyRepo.findAll(sort);
        model.addAttribute("companyList", companyList);
        return "company/index";
    }

    @GetMapping("/edit")
    public String editCompany(@RequestParam Integer id, Model model) {
        Optional<CompanyDTO> dtoOptional = companyService.getCompanyDTOById(id);

        if (dtoOptional.isEmpty()) {
            return "redirect:/company";
        }
        model.addAttribute("companyDTO", dtoOptional.get());
        model.addAttribute("id", id);
        model.addAttribute("countryList", countyService.getCountryList());

        return "company/edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<ApiMessage> editCompany(@RequestParam Integer id,
                                                  @Valid @ModelAttribute CompanyDTO companyDTO) {
        companyService.updateCompany(id, companyDTO);

        return ResponseEntity.ok(new ApiMessage(
                "success",
                "Company Updated",
                "Company updated successfully",
                Collections.emptyMap()));
    }

    @GetMapping("/display")
    public String displayCompany(@RequestParam Integer id, Model model) {
        return companyService.displayCompany(id, model);
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<ApiMessage> addCompany() {
        Country country = countryRepo.findFirstBy()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No country record in DB",
                        "No countries found. Need a country before adding new Company"));
        Company newCompany = companyService.addNewCompany(country, companyService.getLastCompanyId());
        return  ResponseEntity.ok(new ApiMessage(
                "success",
                "Add success",
                "New company blueprint added successfully",
                Map.of("id", newCompany.getId().toString())
        ));
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<ApiMessage> deleteCompany(@RequestParam Integer id) {
        return companyService.deleteCompanyById(id);
    }
}