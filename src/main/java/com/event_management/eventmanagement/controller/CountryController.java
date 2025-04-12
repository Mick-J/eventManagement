package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.model.Country;
import com.event_management.eventmanagement.model.CountryDTO;
import com.event_management.eventmanagement.repository.CompanyRepository;
import com.event_management.eventmanagement.repository.CountryRepository;
import com.event_management.eventmanagement.service.CountyService;
import com.event_management.eventmanagement.utils.FormMessage;
import com.event_management.eventmanagement.utils.Message;
import com.event_management.eventmanagement.utils.SanitizationUtils;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/country")
public class CountryController {

    private static final Logger logger = LogManager.getLogger(CountryController.class);
    private final CountryRepository countryRepo;
    private final CompanyRepository companyRepo;
    private final CountyService countyService;

    @Autowired
    public CountryController(CountryRepository countryRepo,
                             CompanyRepository companyRepo, CountyService countyService) {
        this.countryRepo = countryRepo;
        this.countyService = countyService;
        this.companyRepo = companyRepo;
    }


    @GetMapping({"", "/"})
    public String getCountryList(Model model) {
        List<Country> countryList = countyService.getCountryList();
        model.addAttribute("countryList", countryList);
        return "country/index";
    }


    @GetMapping("/edit")
    public String editCounty(Model model, @RequestParam Integer id) {
        Country country = countryRepo.findById(id).orElse(null);

        if (country == null) {
            return "/country/index";
        }

        //
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setCountryName(country.getCountryName());
        countryDTO.setCountryCode(country.getCountryCode());
        countryDTO.setCountryDialCode(country.getCountryDialCode());

        //
        try {
            SanitizationUtils.sanitizeObjectFields(countryDTO, "unescape");
        } catch (Exception ex) {
            logger.info("Error while sanitizing {} ", ex.getMessage());
        }
        model.addAttribute("countryDTO", countryDTO);
        model.addAttribute("id", country.getId());
//        model.addAttribute("country", country);
        return "/country/edit";
    }


    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> editCountry( @RequestParam Integer id,
                              @Valid @ModelAttribute CountryDTO countryDTO,
                              BindingResult result
    ) {
        String objectName = "Country";
        Country country = countryRepo.findById(id).orElse(null);

        if (country == null) {
            logger.info("Country with id: {} not found when editing", id);
            Message message = FormMessage.withMessage(null, "error", "Something went wrong",
                    objectName + " not updated");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
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

        // update country
        country.setCountryName(countryDTO.getCountryName());
        country.setCountryCode(countryDTO.getCountryCode());
        country.setCountryDialCode(countryDTO.getCountryDialCode());

        try {
            SanitizationUtils.sanitizeObjectFields(countryDTO, "escape");
            countryRepo.save(country);
            Message message = FormMessage.withMessage(null, "success", objectName + " update",
                    objectName + " updated successfully!");
            return ResponseEntity.ok(message);
        } catch (DataAccessException ex) {
            Throwable rootCause = ex.getRootCause();
            String errorMessage = (rootCause != null) ? rootCause.getMessage() : "Unknown SQL Error";
            logger.error("SQL Error: {}", errorMessage);
        } catch (Exception ex) {
            logger.info(" Save country fail {}" , ex.getMessage());
        }

        Message message = FormMessage.withMessage(null, "error", "Something went wrong",
                objectName + " not updated");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @GetMapping("/display")
    public String displayCounty(Model model, @RequestParam Integer id) {
        Country country = countryRepo.findById(id).orElse(null);

        if (country == null) {
            return "redirect:country/index";
        }
        //
        try {
            SanitizationUtils.sanitizeObjectFields(country, "unescape");
        } catch (Exception e) {
            logger.info("Error while unescape html tags in country object with id = {}", country.getId());
        }
        model.addAttribute("country", country);

        return "country/display";
    }

    @GetMapping("/add")
    public RedirectView addCounty( Model model, RedirectAttributes attributes) {
        Country country = new Country("--New--", "N/A", "00",
                                    Instant.now(), Instant.now());

        try {
            Country newCountry = countryRepo.save(country);
            CountryDTO countryDTO = new CountryDTO();
            countryDTO.setCountryName(newCountry.getCountryName());
            countryDTO.setCountryCode(newCountry.getCountryCode());
            countryDTO.setCountryDialCode(newCountry.getCountryDialCode());

            attributes.addAttribute("id", newCountry.getId());
            model.addAttribute("countryDTO", countryDTO);

            return new RedirectView("/country/edit");
        } catch (Exception ex) {
            logger.info("Something went wrong when adding new country");
        }
        return new RedirectView("/country/index");
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<Message> deleteCountry(@RequestParam Integer id) {
        boolean isUsed = companyRepo.existsByCountryId(id);
        if (!isUsed) {
            try {
                countryRepo.deleteById(id);
                Message message = new Message.Builder()
                        .withMessageType("success")
                        .withMessageHeader("Delete success")
                        .withMessageBody("Country deleted successfully!")
                        .build();
                return ResponseEntity.ok(message);
            } catch (Exception e) {
                Message message = new Message.Builder()
                        .withMessageType("error")
                        .withMessageHeader("Something went wrong")
                        .withMessageBody("Country not delete")
                        .build();
                logger.info("Something went wrong when deleting country with id = {}", id);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
            }
        }

        //
        Message message = new Message.Builder()
                .withMessageType("error")
                .withMessageHeader("Delete error")
                .withMessageBody("Cannot delete, a company has this country set.")
                .build();
        return ResponseEntity.badRequest().body(message);
    }
}