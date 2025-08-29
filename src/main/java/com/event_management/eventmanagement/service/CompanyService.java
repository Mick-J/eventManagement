package com.event_management.eventmanagement.service;

import com.event_management.eventmanagement.DTO.CompanyDTO;
import com.event_management.eventmanagement.controllerAdvice.DuplicateResourceException;
import com.event_management.eventmanagement.controllerAdvice.ResourceNotFoundException;
import com.event_management.eventmanagement.model.Company;
import com.event_management.eventmanagement.model.Country;
import com.event_management.eventmanagement.repository.CompanyRepository;
import com.event_management.eventmanagement.repository.EventRepository;
import com.event_management.eventmanagement.repository.UserCompanyRepository;
import com.event_management.eventmanagement.utils.ApiMessage;
import com.event_management.eventmanagement.utils.SanitizationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

@Service
public class CompanyService {
    private static final Logger logger = LogManager.getLogger(CompanyService.class);

    private final CompanyRepository companyRepo;
    private final UserCompanyRepository userCompanyRepo;
    private final EventRepository eventRepo;
    private final CountryService countryService;

    public CompanyService(CompanyRepository companyRepo,
                          UserCompanyRepository userCompanyRepo,
                          EventRepository eventRepo, CountryService countryService
    ) {
        this.companyRepo = companyRepo;
        this.userCompanyRepo = userCompanyRepo;
        this.eventRepo = eventRepo;
        this.countryService = countryService;
    }

    public Optional<CompanyDTO> getCompanyDTOById(Integer id) {
        return companyRepo.findById(id).map(company -> {
            CompanyDTO dto = new CompanyDTO(
                    company.getId(),
                    company.getCompanyName(),
                    company.getWebsite(),
                    company.getContactEmail(),
                    company.getCompanyPhone(),
                    company.getCompanyFax(),
                    company.getCountry().getId()
            );
            try {
                SanitizationUtils.sanitizeObjectFields(dto, "unescape");
            } catch (Exception ex) {
                logger.warn("Error while sanitizing DTO: {}", ex.getMessage());
            }
            return dto;
        });
    }

    public Company getCompanyByIdOrThrow(Integer id) {
        return companyRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Company not found with ID " + id,
                        "Company not found"));
    }



    public boolean isEmailTaken(String email, Integer currentCompanyId) {
        return companyRepo.findByContactEmail(email)
                .filter(c -> !c.getId().equals(currentCompanyId))
                .isPresent();
    }

    public boolean isCompanyNameTaken(CompanyDTO currentCompanyDTO) {
        return companyRepo.findByCompanyName(currentCompanyDTO.companyName().strip())
                .filter(c -> !c.getId().equals(currentCompanyDTO.id()))
                .isPresent();
    }

    @Transactional
    public void updateCompany(Integer id, CompanyDTO companyDTO) {
        // Fetch the company
        Company company = getCompanyByIdOrThrow(id);

        // Fetch the country
        Country country = countryService.getCountryByIdOrThrow(companyDTO.countryId());

        // Check for email uniqueness
        if (isEmailTaken(companyDTO.contactEmail(), companyDTO.id())) {
            throw new DuplicateResourceException(
                    "Email: "+ companyDTO.contactEmail() + " is already in use when updating company ID = " + companyDTO.countryId(),
                    "Email is already used by another company."
            );
        }

        // Check for company name uniqueness
        if (isCompanyNameTaken(companyDTO)) {
            throw new DuplicateResourceException(
                    "Company name: "+ companyDTO.companyName() + " is already in use when updating company ID = " + companyDTO.countryId(),
                    "Company name already used by another company."
            );
        }

        // Update the entity
        company.setCountry(country);
        company.setCompanyName(companyDTO.companyName());
        company.setWebsite(companyDTO.website());
        company.setContactEmail(companyDTO.contactEmail());
        company.setCompanyPhone(companyDTO.companyPhone());
        company.setCompanyFax(companyDTO.companyFax());

        // Sanitize and save
        SanitizationUtils.sanitizeObjectFields(company, "escape");
        companyRepo.save(company);
    }

    public String displayCompany(Integer id, Model model) {
        Company company = companyRepo.findById(id).orElse(null);

        if (company == null) {
            return "redirect:company/index";
        }
        try {
            SanitizationUtils.sanitizeObjectFields(company, "unescape");
        } catch (Exception e) {
            logger.info(
                    "Error while unescape html tags in Company object with id = {}",
                    company.getId());
        }
        model.addAttribute("company", company);

        return "company/display";
    }

    public ResponseEntity<ApiMessage> deleteCompanyById(Integer id) {
        //
        if (eventRepo.existsByCompanyId(id) || userCompanyRepo.existsByCompanyId(id)) {
            return new ResponseEntity<>(new ApiMessage(
                    "error",
                    "Delete Error",
                    "Cannot delete. Company is in use.",
                    Collections.emptyMap()), HttpStatus.CONFLICT);
        }

        //
        companyRepo.deleteById(id);

        return ResponseEntity.ok(new ApiMessage(
                "success",
                "Delete Success",
                "Company deleted successfully",
                Collections.emptyMap()));
    }

    public Company addNewCompany(Country country, int lastCompanyId) {
        Company company = new Company(
                "--New" + (1 + lastCompanyId) + "--",
                "N/A",
                "example" + (1 + lastCompanyId) + "@tech.com",
                "00000000",
                "00000000",
                Instant.now(),
                Instant.now(),
                country);
        return companyRepo.save(company);
    }

    public int getLastCompanyId() {
        Company company = companyRepo.findTopByOrderByIdDesc();
        return (company != null) ? company.getId(): 1;
    }

    public void addModelCompanyList(Model model) {
        Sort sort = Sort.by(Sort.Direction.ASC, "companyName");
        model.addAttribute("companyList", companyRepo.findAll(sort));
    }
}