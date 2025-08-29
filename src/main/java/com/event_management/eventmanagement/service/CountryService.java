package com.event_management.eventmanagement.service;

import com.event_management.eventmanagement.DTO.CountryDTO;
import com.event_management.eventmanagement.DTO.EventTypeDTO;
import com.event_management.eventmanagement.controllerAdvice.DuplicateResourceException;
import com.event_management.eventmanagement.controllerAdvice.ResourceNotFoundException;
import com.event_management.eventmanagement.model.Country;
import com.event_management.eventmanagement.repository.CompanyRepository;
import com.event_management.eventmanagement.repository.CountryRepository;
import com.event_management.eventmanagement.utils.ApiMessage;
import com.event_management.eventmanagement.utils.SanitizationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    private static final Logger logger = LogManager.getLogger(CountryService.class);

    private final CountryRepository countryRepo;
    private final CompanyRepository companyRepo;

    public CountryService(CountryRepository countryRepo,
                          CompanyRepository companyRepo) {
        this.countryRepo = countryRepo;
        this.companyRepo = companyRepo;
    }

    public List<Country> getCountryList() {
        return countryRepo.findAll(Sort.by(Sort.Direction.ASC, "countryName"));
    }

    public Optional<CountryDTO> getCountryDTOById(Integer id) {
        return countryRepo.findById(id).map(country -> {
            CountryDTO countryDTO = new CountryDTO(
                    country.getId(),
                    country.getCountryName(),
                    country.getCountryCode(),
                    country.getCountryDialCode()
            );
            try {
                SanitizationUtils.sanitizeObjectFields(countryDTO, "unescape");
            } catch (Exception ex) {
                logger.warn("Error while sanitizing DTO: {}", ex.getMessage());
            }
            return countryDTO;
        });
    }

    @Transactional
    public void updateCountry(Integer id, CountryDTO countryDTO) {
        // Fetch the country
        Country country = countryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Country with ID " + id + " not found.",
                        "Country not found, please contact the admin"));

        // Check for country name uniqueness
        if (isCountryNameTaken(countryDTO)) {
            throw new DuplicateResourceException(
                    "Country name: "+ countryDTO.countryName() + " already exist when updating Country with ID = " + countryDTO.id(),
                    "Country name already exist."
            );
        }
        // Update country
        country.setCountryName(countryDTO.countryName());
        country.setCountryCode(countryDTO.countryCode());
        country.setCountryDialCode(countryDTO.countryDialCode());

        // Sanitize and save
        SanitizationUtils.sanitizeObjectFields(country, "escape");
        countryRepo.save(country);
    }

    public String displayCounty(Integer id, Model model) {
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

    public ResponseEntity<ApiMessage> deleteCountryById(Integer id) {
        //
        boolean isCountryUsed = companyRepo.existsByCountryId(id);
        if (isCountryUsed) {
            return ResponseEntity.badRequest().body(new ApiMessage(
                    "error",
                    "Delete Error",
                    "Cannot delete. Country is in use.",
                    Collections.emptyMap()));
        }

        //
        countryRepo.deleteById(id);

        return ResponseEntity.ok(new ApiMessage(
                "success",
                "Delete Success",
                "Country deleted successfully",
                Collections.emptyMap()));
    }

    public int getLastCountryId() {
        Country country = countryRepo.findTopByOrderByIdDesc();
        return (country != null) ? country.getId(): 1;
    }

    public Country addNewCountry(int lastId) {
        Country country = new Country("--New" + (1 + lastId) + "--", "N/A", "00",
                Instant.now(), Instant.now());
        return countryRepo.save(country);
    }

    public Country getCountryByIdOrThrow(Integer id) {
        return countryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Country not found with ID " + id,
                        "Country not found with"));
    }

    public boolean isCountryNameTaken(CountryDTO countryDTO) {
        return countryRepo.findCountriesByCountryName(countryDTO.countryName().strip())
                .filter(e -> !e.getId().equals(countryDTO.id()))
                .isPresent();
    }
}