package com.event_management.eventmanagement.repository;

import com.event_management.eventmanagement.model.UserCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCompanyRepository extends JpaRepository<UserCompany, Integer> {
    boolean existsByCompanyId(Integer companyId);
}
