package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.model.Company;
import com.event_management.eventmanagement.model.Country;
import com.event_management.eventmanagement.repository.CompanyRepository;
import com.event_management.eventmanagement.repository.CountryRepository;
import com.event_management.eventmanagement.repository.EventRepository;
import com.event_management.eventmanagement.repository.UserCompanyRepository;
import com.event_management.eventmanagement.service.CompanyService;
import com.event_management.eventmanagement.service.CountyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class CompanyControllerTest {

    private MockMvc mockMvc;

    @Mock private CompanyRepository companyRepo;
    @Mock private CountryRepository countryRepo;
    @Mock private CountyService countyService;

    @InjectMocks
    private CompanyController companyController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(companyController).build();
    }

    @Test
    @DisplayName("test Edit Company >>GetRequest >>Company Exists")
    void testEditCompany_GetRequest_CompanyExists() throws Exception {
        //
        Company mockCompany = new Company();
        mockCompany.setId(1);
        mockCompany.setCompanyName("Test Company name");
        mockCompany.setContactEmail("test@techgermany.com");
        mockCompany.setCountry(new Country(1, "Germany"));
        //
        when(companyRepo.findById(1)).thenReturn(Optional.of(mockCompany));
        when(countyService.getCountryList()).thenReturn(List.of(new Country(1, "Germany")));
        //
        mockMvc.perform(get("/company/edit").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/edit"))
                .andExpect(model().attributeExists("companyDTO"))
                .andExpect(model().attributeExists("countryList"));
    }

    @Test
    @DisplayName("test Edit Company >>GetRequest >>Company Not Found")
    void testEditCompany_GetRequest_CompanyNotFound() throws Exception {
        //
        when(companyRepo.findById(999)).thenReturn(Optional.empty());
        //
        mockMvc.perform(get("/company/edit").param("id", "999"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/index"));
    }


    @Test
    @DisplayName("test Edit Company >>PostRequest >>Success")
    void testEditCompany_PostRequest_Success() throws Exception {
        //
        int companyId = 1;
        int countryId = 100;

        Company existingCompany = new Company();
        existingCompany.setId(companyId);
        existingCompany.setContactEmail("old@tech.com");

        Country country = new Country();
        country.setId(countryId);

        when(companyRepo.findById(companyId)).thenReturn(Optional.of(existingCompany));
        when(countryRepo.findById(countryId)).thenReturn(Optional.of(country));
        when(companyRepo.findByContactEmail("new@tech.com")).thenReturn(Optional.empty());
        when(companyRepo.save(any(Company.class))).thenReturn(existingCompany);

        //
        mockMvc.perform(post("/company/edit")
                        .param("id", String.valueOf(companyId))
                        .param("companyName", "Test Company name")
                        .param("companyPhone", "123456789")
                        .param("companyFax", "987654321")
                        .param("website", "https://techgermany.com")
                        .param("contactEmail", "new@tech.com")
                        .param("countryId", String.valueOf(countryId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageType").value("success"))
                .andExpect(jsonPath("$.messageBody").value("Company updated successfully!"));

        // Verify saved entity
        verify(companyRepo).save(any(Company.class));
    }

    @Test
    @DisplayName("test Edit Company >>PostRequest >>Country Not Found")
    void testEditCompany_PostRequest_CountryNotFound() throws Exception {
        int companyId = 1;
        int invalidCountryId = 999;

        Company company = new Company();
        company.setId(companyId);
        company.setContactEmail("company@tech.com");

        //
        when(companyRepo.findById(companyId)).thenReturn(Optional.of(company));
        when(countryRepo.findById(invalidCountryId)).thenReturn(Optional.empty());
        //
        mockMvc.perform(post("/company/edit")
                        .param("id", String.valueOf(companyId))
                        .param("companyName", "Test Company name")
                        .param("companyPhone", "123456789")
                        .param("companyFax", "987654321")
                        .param("website", "https://techgermany.com")
                        .param("contactEmail", "new@tech.com")
                        .param("countryId", String.valueOf(invalidCountryId))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageType").value("error"))
                .andExpect(jsonPath("$.messageBody").value("Company not updated"));
    }

    @Test
    @DisplayName("test Edit Company >>PostRequest >>Duplicate Email")
    void testEditCompany_PostRequest_DuplicateEmail() throws Exception {
        int companyId = 1;
        int countryId = 2;

        Company currentCompany = new Company();
        currentCompany.setId(companyId);
        currentCompany.setContactEmail("original@tech.com");

        Company otherCompany = new Company();
        otherCompany.setId(999);
        otherCompany.setContactEmail("duplicate@tech.com");

        Country country = new Country();
        country.setId(countryId);
        //
        when(companyRepo.findById(companyId)).thenReturn(Optional.of(currentCompany));
        when(countryRepo.findById(countryId)).thenReturn(Optional.of(country));
        when(companyRepo.findByContactEmail("duplicate@tech.com")).thenReturn(Optional.of(otherCompany));

        mockMvc.perform(post("/company/edit")
                        .param("id", String.valueOf(companyId))
                        .param("companyName", "Test Company name")
                        .param("companyPhone", "123456789")
                        .param("companyFax", "987654321")
                        .param("website", "https://techgermany.com")
                        .param("contactEmail", "duplicate@tech.com")
                        .param("countryId", String.valueOf(countryId))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageType").value("error"))
                .andExpect(jsonPath("$.messageBody").value("Company not updated"))
                .andExpect(jsonPath("$.detailList.contactEmail").value("Email Already used by another company"));
    }


    @Test
    @DisplayName("test Edit Company >>PostRequest >>Missing Company Name")
    void testEditCompany_PostRequest_MissingCompanyName() throws Exception {
        int companyId = 1;
        int countryId = 2;

        Company company = new Company();
        company.setId(companyId);
        company.setContactEmail("email@tech.com");

        Country country = new Country();
        country.setId(countryId);
        //
        when(companyRepo.findById(companyId)).thenReturn(Optional.of(company));
        when(countryRepo.findById(countryId)).thenReturn(Optional.of(country));
        when(companyRepo.findByContactEmail("new@tech.com")).thenReturn(Optional.empty());

        //
        mockMvc.perform(post("/company/edit")
                        .param("id", String.valueOf(companyId))
                        .param("companyName", "")
                        .param("companyPhone", "123456789")
                        .param("companyFax", "987654321")
                        .param("website", "https://techgermany.com")
                        .param("contactEmail", "new@tech.com")
                        .param("countryId", String.valueOf(countryId))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageType").value("error"))
                .andExpect(jsonPath("$.messageBody").value("Company not updated"))
                .andExpect(jsonPath("$.detailList.companyName").exists());
    }

    @Test
    @DisplayName("test Edit Company >>PostRequest >>DB Failure")
    void testEditCompany_PostRequest_DBFailure() throws Exception {
        int companyId = 1;
        int countryId = 2;

        Company company = new Company();
        company.setId(companyId);
        company.setContactEmail("test@techgermany.com");

        Country country = new Country();
        country.setId(countryId);
        //
        when(companyRepo.findById(companyId)).thenReturn(Optional.of(company));
        when(countryRepo.findById(countryId)).thenReturn(Optional.of(country));
        when(companyRepo.findByContactEmail("test@techgermany.com")).thenReturn(Optional.of(company));
        when(companyRepo.save(any())).thenThrow(new DataAccessException("DB Error") {});

        //
        mockMvc.perform(post("/company/edit")
                        .param("id", String.valueOf(companyId))
                        .param("companyName", "Updated Company name")
                        .param("companyPhone", "123456789")
                        .param("companyFax", "987654321")
                        .param("website", "https://techgermany.com")
                        .param("contactEmail", "test@techgermany.com")
                        .param("countryId", String.valueOf(countryId))
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.messageType").value("error"))
                .andExpect(jsonPath("$.messageBody").value("Company not updated"));
    }
}