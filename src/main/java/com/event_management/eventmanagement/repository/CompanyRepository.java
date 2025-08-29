package com.event_management.eventmanagement.repository;

import com.event_management.eventmanagement.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    boolean existsByCountryId(Integer id);

    Company findTopByOrderByIdDesc();

    Optional<Company> findByContactEmail(String contactEmail);

    Optional<Company> findByCompanyName(String companyName);

}