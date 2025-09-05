package com.event_management.eventmanagement;

import com.event_management.eventmanagement.config.TestSecurityConfig;
import com.event_management.eventmanagement.model.Country;
import com.event_management.eventmanagement.model.User;
import com.event_management.eventmanagement.repository.CompanyRepository;
import com.event_management.eventmanagement.repository.CountryRepository;
import com.event_management.eventmanagement.repository.UserRepository;
import com.event_management.eventmanagement.service.CompanyService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class CompanyIntegrationTest {

    @Container
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>("mariadb:10.11")
            .withDatabaseName("test_eventdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DynamicPropertySource
    static void dbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariaDB::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDB::getUsername);
        registry.add("spring.datasource.password", mariaDB::getPassword);
        registry.add("spring.datasource.driver-class-name", mariaDB::getDriverClassName);
    }

    @BeforeEach
    void setupUser() {
        if (userRepository.findByUserEmail("michelletim@techchina.com").isEmpty()) {
            User testUser = new User();
            testUser.setUserEmail("michelletim@techchina.com");
            testUser.setUserPassword(passwordEncoder.encode("password"));
            testUser.setUserFirstname("Michelle");
            testUser.setUserLastname("Tim");
            testUser.setUserActive("Y");
            testUser.setUserRole("USER");
            userRepository.save(testUser);
        }
        // Ensure at least one country exists
        if (countryRepo.count() == 0) {
            Country country = new Country();
            country.setCountryName("TestCountry");
            countryRepo.save(country);
        }
    }

    private RequestPostProcessor authUser(String email) {
        User userEntity = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(userEntity.getUserEmail())
                .password(userEntity.getUserPassword())
                .roles(userEntity.getUserRole())
                .build();

        return user(userDetails);
    }

    @Test
    void testGetCompanyListAuthenticated() throws Exception {
        mockMvc.perform(get("/company").with(authUser("michelletim@techchina.com")))
                .andExpect(status().isOk());
    }

    @Test
    void testFullFlow_addDisplayEditDelete() throws Exception {
        String userEmail = "michelletim@techchina.com";

        // 1. Add a company
        MvcResult addResult = mockMvc.perform(post("/company/add")
                        .with(csrf())
                        .with(authUser(userEmail)))
                .andExpect(status().isOk())
                .andReturn();

        // 2. Extract ID from response
        String json = addResult.getResponse().getContentAsString();
        String id = JsonPath.read(json, "$.detailList.id");
        String companyId = JsonPath.read(json, "$.detailList.id");

        // 3. Display the added company
        mockMvc.perform(get("/company/display")
                        .param("id", id)
                        .with(authUser(userEmail)))
                .andExpect(status().isOk());

        // 4. Edit company (GET)
        mockMvc.perform(get("/company/edit")
                        .param("id", companyId)
                        .with(authUser(userEmail)))
                .andExpect(status().isOk())
                .andExpect(view().name("company/edit"))
                .andExpect(model().attributeExists("companyDTO"))
                .andExpect(model().attributeExists("id"))
                .andExpect(model().attributeExists("countryList"));

        // 5. Edit the company (POST)
        mockMvc.perform(post("/company/edit")
                        .param("id", id)
                        .param("companyName", "Test Company Updated")
                        .param("website", "https://tech.netherland.com")
                        .param("contactEmail", "updatedemail@tech.netherland.com")
                        .param("companyPhone", "1234567890")
                        .param("companyFax", "0987654321")
                        .param("countryId", "1")
                        .with(csrf())
                        .with(authUser(userEmail)))
                .andExpect(status().isOk());

        // 6. Delete the company
        mockMvc.perform(delete("/company/delete")
                        .param("id", id)
                        .with(csrf())
                        .with(authUser(userEmail)))
                .andExpect(status().isOk());
    }

}