package com.online_store.backend.api.profile.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.profile.dto.request.ProfileRequestDto;
import com.online_store.backend.api.profile.dto.response.ProfileResponseDto;
import com.online_store.backend.api.profile.service.ProfileService;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_CUSTOMER')")
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<String>> createProfile(
            @Valid @RequestPart("profile") ProfileRequestDto profileRequestDto,
            @RequestPart("file") MultipartFile file) {
        profileService.createProfile(profileRequestDto, file);
        return ResponseEntity.ok(ApiResponse.success("Profile created successfully."));
    }

    @PutMapping("")
    public ResponseEntity<ApiResponse<String>> updateProfile(
            @Valid @RequestPart("profile") ProfileRequestDto profileRequestDto,
            @RequestPart("file") MultipartFile file) {
        profileService.updateProfile(profileRequestDto, file);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully."));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<ProfileResponseDto>> getCurrentUserProfile() {
        ProfileResponseDto profile = profileService.getCurrentUserProfile();
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully.", profile));
    }

}
