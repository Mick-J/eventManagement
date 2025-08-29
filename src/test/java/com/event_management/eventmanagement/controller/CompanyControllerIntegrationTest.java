package com.event_management.eventmanagement.controller;

import com.event_management.eventmanagement.config.TestSecurityConfig;
import com.event_management.eventmanagement.model.Company;
import com.event_management.eventmanagement.model.Country;
import com.event_management.eventmanagement.model.User;
import com.event_management.eventmanagement.repository.CompanyRepository;
import com.event_management.eventmanagement.repository.CountryRepository;
import com.event_management.eventmanagement.repository.UserRepository;
import com.event_management.eventmanagement.utils.ApiMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.TestPropertySource;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@Transactional
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class CompanyControllerIntegrationTest {

    @Container
    static MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.11")
            .withDatabaseName("test_eventdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withInitScript("init-test-db.sql");

    @LocalServerPort 
    private int port;

    @Autowired 
    private TestRestTemplate restTemplate;

    @Autowired 
    private CompanyRepository companyRepo;

    @Autowired 
    private CountryRepository countryRepo;
    
    @Autowired 
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/company";
        companyRepo.deleteAll();
        countryRepo.deleteAll();
        createTestUser();
    }

    private void createTestUser() {
        if (userRepository.findByUserEmail("michelletim@techchina.com").isPresent()) {
            return;
        }

        User testUser = new User();
        testUser.setUserEmail("michelletim@techchina.com");
        testUser.setUserPassword(passwordEncoder.encode("password")); // Use encoded password
        testUser.setUserFirstname("Michelle");
        testUser.setUserLastname("Tim");
        testUser.setUserActive("Y");
        testUser.setUserRole("USER");
        userRepository.save(testUser);
    }

    private HttpHeaders getAuthenticatedHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String credentials = "michelletim@techchina.com:password";
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedCredentials);
        return headers;
    }

    @Test
    void testBasicAuthLogin() {
        HttpHeaders headers = getAuthenticatedHeaders();
        HttpEntity<Void> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/dashboard",
                HttpMethod.GET,
                request,
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testConnectionEstablished() {
        assertThat(mariaDBContainer.isCreated()).isTrue();
        assertThat(mariaDBContainer.isRunning()).isTrue();
        assertThat(mariaDBContainer.getDatabaseName()).isEqualTo("test_eventdb");
    }

    @Test
    void testAddCompany() {
        Country country = new Country();
        country.setCountryName("Venezuela");
        country = countryRepo.save(country);
        assertThat(country.getId()).isNotNull();

        HttpEntity<Void> request = new HttpEntity<>(null, getAuthenticatedHeaders());
        ResponseEntity<ApiMessage> response = restTemplate.postForEntity(
                baseUrl + "/add", request, ApiMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessageType()).isEqualTo("success");

        String companyId = response.getBody().getDetailList().get("id");
        assertThat(companyId).isNotNull();
        Optional<Company> savedCompany = companyRepo.findById(Integer.valueOf(companyId));
        assertThat(savedCompany).isPresent();
    }

    @Test
    void testDeleteCompany() {
        Country country = new Country();
        country.setCountryName("Jamaica");
        country = countryRepo.save(country);

        Company company = new Company();
        company.setCompanyName("Tech Jamaica");
        company.setCountry(country);
        company = companyRepo.save(company);

        HttpEntity<Void> request = new HttpEntity<>(null, getAuthenticatedHeaders());
        ResponseEntity<ApiMessage> response = restTemplate.exchange(
                baseUrl + "/delete?id=" + company.getId(),
                HttpMethod.DELETE,
                request,
                ApiMessage.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(companyRepo.existsById(company.getId())).isFalse();
    }

    @Test
    void testGetCompanyList() {
        Country country1 = new Country();
        country1.setCountryName("USA");
        country1 = countryRepo.save(country1);

        Country country2 = new Country();
        country2.setCountryName("Canada");
        country2 = countryRepo.save(country2);

        Company company1 = new Company();
        company1.setCompanyName("Tech USA");
        company1.setCountry(country1);
        companyRepo.save(company1);

        Company company2 = new Company();
        company2.setCompanyName("Tech Canada");
        company2.setCountry(country2);
        companyRepo.save(company2);

        HttpEntity<Void> request = new HttpEntity<>(null, getAuthenticatedHeaders());
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl, HttpMethod.GET, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Tech USA");
        assertThat(response.getBody()).contains("Tech Canada");
    }
}