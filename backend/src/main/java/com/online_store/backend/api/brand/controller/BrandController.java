package com.online_store.backend.api.brand.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.brand.dto.request.BrandRequestDto;
import com.online_store.backend.api.brand.dto.response.BrandResponseDto;
import com.online_store.backend.api.brand.service.BrandService;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/brand")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> createBrand(
            @Valid @RequestPart("brand") BrandRequestDto brandRequestDto,
            @RequestPart("file") MultipartFile file) {
        brandService.createBrand(brandRequestDto, file);
        return ResponseEntity.ok(ApiResponse.success("Brand created successfully."));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<BrandResponseDto>>> getAllBrand() {
        return ResponseEntity.ok(ApiResponse.success(
                "Brands retrieved successfully.",
                brandService.getBrands()));
    }
}
