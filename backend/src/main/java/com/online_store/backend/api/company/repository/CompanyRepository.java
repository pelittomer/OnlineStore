package com.online_store.backend.api.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.company.entity.Company;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String name);

    Optional<Company> findByTaxId(String taxId);
}
