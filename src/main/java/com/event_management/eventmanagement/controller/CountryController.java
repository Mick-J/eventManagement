package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.DTO.CountryDTO;
import com.event_management.eventmanagement.controllerAdvice.ResourceNotFoundException;
import com.event_management.eventmanagement.model.Company;
import com.event_management.eventmanagement.model.Country;
import com.event_management.eventmanagement.service.CountryService;
import com.event_management.eventmanagement.utils.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/country")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping({"", "/"})
    public String getCountryList(Model model) {
        List<Country> countryList = countryService.getCountryList();
        model.addAttribute("countryList", countryList);
        return "country/index";
    }

    @GetMapping("/edit")
    public String editCounty(@RequestParam Integer id, Model model) {
        Optional<CountryDTO> dtoOptional = countryService.getCountryDTOById(id);

        if (dtoOptional.isEmpty()) {
            return "redirect:/country";
        }

        model.addAttribute("countryDTO", dtoOptional.get());
        model.addAttribute("id", id);

        return "/country/edit";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<ApiMessage> addCountry() {
        Country country = countryService.addNewCountry(1 + countryService.getLastCountryId());
        return  ResponseEntity.ok(new ApiMessage(
                "success",
                "Add success",
                "New company blueprint added successfully",
                Map.of("id", country.getId().toString())
        ));
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<ApiMessage> editCountry( @RequestParam Integer id,
                              @Valid @ModelAttribute CountryDTO countryDTO
    ) {
        countryService.updateCountry(id, countryDTO);

        return ResponseEntity.ok(new ApiMessage(
                "success",
                "Country Updated",
                "Country updated successfully",
                Collections.emptyMap()));
    }

    @GetMapping("/display")
    public String displayCounty(@RequestParam Integer id, Model model) {
        return countryService.displayCounty(id, model);
    }

    @GetMapping("/add")
    public ResponseEntity<ApiMessage> addCounty() {
            Country newCountry = countryService.addNewCountry(countryService.getLastCountryId());
            return  ResponseEntity.ok(new ApiMessage(
                    "success",
                    "Add success",
                    "New country blueprint added successfully",
                    Map.of("id", newCountry.getId().toString())
            ));
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<ApiMessage> deleteCountry(@RequestParam Integer id) {
        return countryService.deleteCountryById(id);
    }
}