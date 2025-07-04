package com.online_store.backend.api.company.utils;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.company.dto.response.CompanyResponseDto;
import com.online_store.backend.api.company.entity.Company;
import com.online_store.backend.api.company.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyUtilsService {
    private final CompanyRepository companyRepository;

    public CompanyResponseDto mapCompanyTorResponseDto(Company company) {
        return CompanyResponseDto.builder()
                .id(company.getId())
                .name(company.getName())
                .logo(company.getLogo().getId())
                .description(company.getDescription())
                .websiteUrl(company.getWebsiteUrl())
                .phone(company.getPhone())
                .email(company.getEmail())
                .address(company.getAddress())
                .taxId(company.getTaxId())
                .taxOffice(company.getTaxOffice())
                .status(company.getStatus())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

    public void validateCompanyUniqueness(String name, String taxId) {
        if (companyRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Company name already exists!");
        }
        if (companyRepository.findByTaxId(taxId).isPresent()) {
            throw new IllegalArgumentException("Tax ID already exists!");
        }
    }
}
