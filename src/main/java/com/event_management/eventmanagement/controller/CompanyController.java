package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.model.Company;
import com.event_management.eventmanagement.model.CompanyDTO;
import com.event_management.eventmanagement.model.Country;
import com.event_management.eventmanagement.repository.CompanyRepository;
import com.event_management.eventmanagement.repository.CountryRepository;
import com.event_management.eventmanagement.repository.EventRepository;
import com.event_management.eventmanagement.repository.UserCompanyRepository;
import com.event_management.eventmanagement.service.CompanyService;
import com.event_management.eventmanagement.service.CountyService;
import com.event_management.eventmanagement.utils.FormMessage;
import com.event_management.eventmanagement.utils.Message;
import com.event_management.eventmanagement.utils.SanitizationUtils;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/company")
public class CompanyController {

    private static final Logger logger = LogManager.getLogger(CompanyController.class);
    private final CompanyRepository companyRepo;
    private final CountryRepository countryRepo;
    private final EventRepository eventRepo;
    private final UserCompanyRepository userCompanyRepo;
    private final CompanyService companyService;
    private final CountyService countyService;

    public CompanyController(CompanyRepository companyRepo,
                             CountryRepository countryRepo,
                             EventRepository eventRepo,
                             UserCompanyRepository userCompanyRepo,
                             CountyService countyService,
                             CompanyService companyService) {
        this.companyRepo = companyRepo;
        this.countryRepo = countryRepo;
        this.eventRepo = eventRepo;
        this.userCompanyRepo = userCompanyRepo;
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
    public String editCompany(Model model, @RequestParam Integer id) {
        Company company = companyRepo.findById(id).orElse(null);

        if (company == null) {
            logger.info("Edit Company with id={} not found", id);
            return "company/index";
        }

        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName(company.getCompanyName());
        companyDTO.setCompanyPhone(company.getCompanyPhone());
        companyDTO.setCompanyFax(company.getCompanyFax());
        companyDTO.setWebsite(company.getWebsite());
        companyDTO.setContactEmail(company.getContactEmail());
        companyDTO.setCountryId(company.getCountry().getId());

        //
        try {
            SanitizationUtils.sanitizeObjectFields(companyDTO, "unescape");
        } catch (Exception ex) {
            logger.info("Error while sanitizing {} ", ex.getMessage());
        }

        model.addAttribute("companyDTO", companyDTO);
        model.addAttribute("id", id);
        List<Country> countryList = countyService.getCountryList();
        if (countryList == null) {
            logger.info("Edit Company id = {},  country list is null", id);
            return "company/index";
        }

        model.addAttribute("countryList", countryList);

        return "company/edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> editCompany( @RequestParam Integer id,
                                    @Valid @ModelAttribute CompanyDTO companyDTO,
                                    BindingResult result)
    {
        String objectName = "Company";
        Company company = companyRepo.findById(id).orElse(null);

        if (company == null) {
            Message message = FormMessage.withMessage(null, "error", "Something went wrong",
                    objectName + " not updated");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }

        Country country = countryRepo.findById(companyDTO.getCountryId()).orElse(null);

        if (country == null) {
            logger.info("Unable to find country when editing company with id = {}", companyDTO.getCountryId());
            result.addError(new FieldError("companyDTO", "countryId","Unable to find selected country"));
        }

        // checking email already used
        Company existingCompanyWithEmail = companyRepo.findByContactEmail(companyDTO.getContactEmail()).orElse(null);
        if (existingCompanyWithEmail != null && !Objects.equals(existingCompanyWithEmail.getId(), company.getId())) {
            result.addError(new FieldError("companyDTO", "contactEmail","Email Already used by another company"));
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

        company.setCountry(country);
        company.setCompanyName(companyDTO.getCompanyName());
        company.setWebsite(companyDTO.getWebsite());
        company.setContactEmail(companyDTO.getContactEmail());
        company.setCompanyPhone(companyDTO.getCompanyPhone());
        company.setCompanyFax(companyDTO.getCompanyFax());

        //
        try {
            SanitizationUtils.sanitizeObjectFields(company, "escape");
            companyRepo.save(company);
            Message message = FormMessage.withMessage(null, "success", objectName + " update",
                    objectName + " updated successfully!");
            return ResponseEntity.ok(message);
        } catch (DataAccessException ex) {
            Throwable rootCause = ex.getRootCause();
            String errorMessage = (rootCause != null) ? rootCause.getMessage() : "Unknown SQL Error";
            logger.error("SQL Error: {}", errorMessage);
        } catch (Exception ex) {
            logger.info("Error while saving company {} ", ex.getMessage());
        }
        Message message = FormMessage.withMessage(null, "error", "Something went wrong",
                objectName + " not updated");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @GetMapping("/display")
    public String displayCompany(Model model, @RequestParam Integer id) {
        Company company = companyRepo.findById(id).orElse(null);

        if (company == null) {
            return "redirect:company/index";
        }
        try {
            SanitizationUtils.sanitizeObjectFields(company, "unescape");
        } catch (Exception e) {
            logger.info("Error while unescape html tags in Company object with id = {}", company.getId());
        }
        model.addAttribute("company", company);

        return "company/display";
    }

    @GetMapping("/add")
    public RedirectView addCompany( Model model, RedirectAttributes attributes) {
        Country country = countryRepo.findFirstBy().orElse(null);

        if (country != null) {
            Company company = new Company("--New"+ (1+ companyService.getLastCompanyId()) +"--",
                    "N/A", "example"+ (1+ companyService.getLastCompanyId()) +"@tech.com",
                    "00000000", "00000000",
                    Instant.now(), Instant.now(), country);

            try {
                Company newCompany = companyRepo.save(company);
                CompanyDTO companyDTO = new CompanyDTO();
                companyDTO.setCompanyName(newCompany.getCompanyName());
                companyDTO.setCompanyPhone(newCompany.getCompanyPhone());
                companyDTO.setCompanyFax(newCompany.getCompanyFax());
                companyDTO.setWebsite(newCompany.getWebsite());
                companyDTO.setContactEmail(newCompany.getContactEmail());
                companyDTO.setCountryId(newCompany.getCountry().getId());

                attributes.addAttribute("id", newCompany.getId());
                model.addAttribute("companyDTO", companyDTO);

                return new RedirectView("/company/edit");
            } catch (Exception ex) {
                logger.info("Something went wrong when adding new company");
            }
        }

        return new RedirectView("/company/index");
    }


    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<Message> deleteCompany(@RequestParam Integer id) {
        boolean isUsedInEvent = eventRepo.existsByCompanyId(id);
        boolean isUsedInCompanyUser = userCompanyRepo.existsByCompanyId(id);
        if (!isUsedInEvent && !isUsedInCompanyUser) {
            try {
                companyRepo.deleteById(id);
                Message message = new Message.Builder()
                        .withMessageType("success")
                        .withMessageHeader("Delete success")
                        .withMessageBody("Company deleted successfully!")
                        .build();
                return ResponseEntity.ok(message);
            } catch (Exception e) {
                Message message = new Message.Builder()
                        .withMessageType("error")
                        .withMessageHeader("Something went wrong")
                        .withMessageBody("Company not deleted")
                        .build();
                logger.info("Something went wrong when deleting company with id = {}", id);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
            }
        }

        //
        Message message = new Message.Builder()
                .withMessageType("error")
                .withMessageHeader("Delete error")
                .withMessageBody("Cannot delete, this company is in use elsewhere.")
                .build();
        return ResponseEntity.badRequest().body(message);
    }
}