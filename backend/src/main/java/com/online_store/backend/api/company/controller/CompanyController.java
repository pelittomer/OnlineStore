package com.online_store.backend.api.company.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.company.dto.request.CompanyRequestDto;
import com.online_store.backend.api.company.dto.request.CompanyStatusRequestDto;
import com.online_store.backend.api.company.dto.request.CompanyUpdateRequestDto;
import com.online_store.backend.api.company.dto.response.CompanyResponseDto;
import com.online_store.backend.api.company.service.CompanyService;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<ApiResponse<String>> createCompany(
            @Valid @RequestPart("company") CompanyRequestDto companyRequestDto,
            @RequestPart("file") MultipartFile file) {
        companyService.createCompany(companyRequestDto, file);
        return ResponseEntity.ok(ApiResponse.success("Company created successfully."));
    }

    @PutMapping("")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<ApiResponse<String>> updateCompany(
            @Valid @RequestPart("company") CompanyUpdateRequestDto companyUpdateRequestDto,
            @RequestPart("file") MultipartFile file) {
        companyService.updateCompany(companyUpdateRequestDto, file);
        return ResponseEntity.ok(ApiResponse.success("Company updated successfully."));
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<ApiResponse<CompanyResponseDto>> getCurrentUserCompany() {
        return ResponseEntity.ok(ApiResponse.success(
                "Company find.",
                companyService.getCurrentUserCompany()));
    }

    @PutMapping("status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> updateCompanyStatus(
            @Valid CompanyStatusRequestDto companyStatusRequestDto) {
        companyService.updateCompanyStatus(companyStatusRequestDto);
        return ResponseEntity.ok(ApiResponse.success("Company status updated successfully."));
    }

    @GetMapping("all")
    public ResponseEntity<ApiResponse<List<CompanyResponseDto>>> getAllCompany() {
        return ResponseEntity.ok(ApiResponse.success(
                "Company find.",
                companyService.getAllCompany()));
    }

}
