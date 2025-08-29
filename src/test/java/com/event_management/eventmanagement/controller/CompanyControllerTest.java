package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.DTO.CompanyDTO;
import com.event_management.eventmanagement.controllerAdvice.ResourceNotFoundException;
import com.event_management.eventmanagement.controllerAdvice.GlobalExceptionHandler;
import com.event_management.eventmanagement.model.Company;
import com.event_management.eventmanagement.model.Country;
import com.event_management.eventmanagement.repository.CompanyRepository;
import com.event_management.eventmanagement.repository.CountryRepository;
import com.event_management.eventmanagement.service.CompanyService;
import com.event_management.eventmanagement.service.CountryService;
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
    @Mock private CountryService countyService;
    @Mock private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(companyController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
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
        when(companyService.getCompanyDTOById(1)).thenReturn(Optional.of(
                new CompanyDTO(
                        1,
                        "Test Company name",
                        "https://techgermany.com",
                        "test@techgermany.com",
                        "123456789",
                        "987654321",
                        1
                )
        ));
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
        when(companyService.getCompanyDTOById(999)).thenReturn(Optional.empty());
        //
        mockMvc.perform(get("/company/edit").param("id", "999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/company"));
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

        doNothing().when(companyService).updateCompany(eq(companyId), any(CompanyDTO.class));

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
                .andExpect(jsonPath("$.messageBody").value("Company updated successfully"));
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
        doAnswer(invocation -> {
            throw new ResourceNotFoundException("Country not found with ID " + invalidCountryId, "Country not found");
        }).when(companyService).updateCompany(eq(companyId), any(CompanyDTO.class));
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
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messageType").value("error"))
                .andExpect(jsonPath("$.messageBody").value("Country not found"));
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
        doAnswer(invocation -> {
            throw new com.event_management.eventmanagement.controllerAdvice.DuplicateResourceException(
                    "Email duplicate",
                    "Email is already used by another company."
            );
        }).when(companyService).updateCompany(eq(companyId), any(CompanyDTO.class));

        mockMvc.perform(post("/company/edit")
                        .param("id", String.valueOf(companyId))
                        .param("companyName", "Test Company name")
                        .param("companyPhone", "123456789")
                        .param("companyFax", "987654321")
                        .param("website", "https://techgermany.com")
                        .param("contactEmail", "duplicate@tech.com")
                        .param("countryId", String.valueOf(countryId))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.messageType").value("error"))
                .andExpect(jsonPath("$.messageBody").value("Email is already used by another company."));
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
        // No service stub; validation will fail before service is called

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
                .andExpect(status().isBadRequest());
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
        doThrow(new DataAccessException("DB Error") {}).when(companyService).updateCompany(eq(companyId), any(CompanyDTO.class));

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
                .andExpect(jsonPath("$.messageBody").value("An error occurred while processing your request. Please contact the admin"));
    }
}