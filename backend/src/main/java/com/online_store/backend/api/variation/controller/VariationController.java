package com.online_store.backend.api.variation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.variation.dto.request.VariationRequestDto;
import com.online_store.backend.api.variation.dto.response.VariationResponseDto;
import com.online_store.backend.api.variation.service.VariationService;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/variation")
@RequiredArgsConstructor
public class VariationController {
    private final VariationService variationService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> createVariation(
            @Valid @RequestBody VariationRequestDto variationRequestDto) {
        variationService.createVariation(variationRequestDto);
        return ResponseEntity.ok(ApiResponse.success("Variation created successfully."));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<VariationResponseDto>>> getVariation(
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(
                "Variation found.",
                variationService.getVariation(categoryId)));
    }

}
