package com.event_management.eventmanagement.service;

import com.event_management.eventmanagement.model.Country;
import com.event_management.eventmanagement.repository.CountryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountyService {

    private final CountryRepository countryRepo;

    public CountyService(CountryRepository countryRepo) {
        this.countryRepo = countryRepo;
    }

    public List<Country> getCountryList() {
        Sort sort = Sort.by(Sort.Direction.ASC, "countryName");
        return countryRepo.findAll(sort);
    }
}
