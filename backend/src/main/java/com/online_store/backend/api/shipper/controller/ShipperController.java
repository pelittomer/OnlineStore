package com.online_store.backend.api.shipper.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.shipper.dto.request.ShipperRequestDto;
import com.online_store.backend.api.shipper.dto.response.ShipperResponseDto;
import com.online_store.backend.api.shipper.service.ShipperService;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/api/shipper")
@RequiredArgsConstructor
public class ShipperController {
    private final ShipperService shipperService;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> createShipper(
            @Valid @RequestPart("shipper") ShipperRequestDto shipperRequestDto,
            @RequestPart("file") MultipartFile file) {
        shipperService.createShipper(shipperRequestDto, file);
        return ResponseEntity.ok(ApiResponse.success("Shipper created successfully."));
    }

    @GetMapping("all")
    @PreAuthorize("hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<ShipperResponseDto>>> getAllShipper() {
        return ResponseEntity.ok(ApiResponse.success(
                "Shipper found.",
                shipperService.getAllShipper()));
    }
}
