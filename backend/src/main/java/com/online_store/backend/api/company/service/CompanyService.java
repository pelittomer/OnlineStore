package com.online_store.backend.api.company.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.company.dto.request.CompanyRequestDto;
import com.online_store.backend.api.company.dto.request.CompanyStatusRequestDto;
import com.online_store.backend.api.company.dto.request.CompanyUpdateRequestDto;
import com.online_store.backend.api.company.dto.response.CompanyResponseDto;
import com.online_store.backend.api.company.entity.Company;
import com.online_store.backend.api.company.repository.CompanyRepository;
import com.online_store.backend.api.company.utils.CompanyUtilsService;
import com.online_store.backend.api.upload.entity.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.api.user.entity.User;
import com.online_store.backend.api.user.repository.UserRepository;
import com.online_store.backend.common.utils.CommonUtilsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CommonUtilsService commonUtilsService;
    private final UploadService uploadService;
    private final CompanyUtilsService companyUtilsService;

    @Transactional
    public void createCompany(CompanyRequestDto companyRequestDto, MultipartFile file) {
        commonUtilsService.checkImageFileType(file);

        User currentUser = commonUtilsService.getCurrentUser();
        if (currentUser.getCompany() != null) {
            throw new RuntimeException("You have already registered a company.");
        }

        companyUtilsService.validateCompanyUniqueness(companyRequestDto.getName(), companyRequestDto.getTaxId());

        Upload upload = uploadService.createFile(file);

        Company company = Company.builder()
                .address(companyRequestDto.getAddress())
                .description(companyRequestDto.getDescription())
                .email(companyRequestDto.getEmail())
                .name(companyRequestDto.getName())
                .phone(companyRequestDto.getPhone())
                .taxId(companyRequestDto.getTaxId())
                .taxOffice(companyRequestDto.getTaxOffice())
                .websiteUrl(companyRequestDto.getWebsiteUrl())
                .logo(upload)
                .build();

        companyRepository.save(company);
        currentUser.setCompany(company);
        userRepository.save(currentUser);
    }

    @Transactional
    public void updateCompany(CompanyUpdateRequestDto companyUpdateRequestDto, MultipartFile file) {
        commonUtilsService.checkImageFileType(file);

        User currentUser = commonUtilsService.getCurrentUser();
        Company company = currentUser.getCompany();
        if (company == null) {
            throw new RuntimeException("No company found for the current user to update.");
        }

        Upload upload = uploadService.createFile(file);

        company.setAddress(companyUpdateRequestDto.getAddress());
        company.setDescription(companyUpdateRequestDto.getDescription());
        company.setEmail(companyUpdateRequestDto.getEmail());
        company.setPhone(companyUpdateRequestDto.getPhone());
        company.setTaxOffice(companyUpdateRequestDto.getTaxOffice());
        company.setWebsiteUrl(companyUpdateRequestDto.getWebsiteUrl());
        company.setLogo(upload);

        companyRepository.save(company);
    }

    public void updateCompanyStatus(CompanyStatusRequestDto companyStatusRequestDto) {
        Company company = companyRepository.findById(companyStatusRequestDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Company not found with ID: " + companyStatusRequestDto.getId()));

        company.setStatus(companyStatusRequestDto.getStatus());
        company.setRejectionReason(companyStatusRequestDto.getRejectionReason());

        companyRepository.save(company);
    }

    public CompanyResponseDto getCurrentUserCompany() {
        User currentUser = commonUtilsService.getCurrentUser();
        Company company = currentUser.getCompany();
        if (company == null) {
            throw new RuntimeException("Company not found for the current user.");
        }

        return companyUtilsService.mapCompanyTorResponseDto(company);
    }

    public List<CompanyResponseDto> getAllCompany() {
        Pageable pageable = PageRequest.of(0, 30);
        List<Company> allCompanies = companyRepository.findAll(pageable).getContent();
        return allCompanies.stream().map(companyUtilsService::mapCompanyTorResponseDto).toList();
    }

}
