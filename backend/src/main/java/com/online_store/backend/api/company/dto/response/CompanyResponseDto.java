package com.online_store.backend.api.company.dto.response;

import java.time.LocalDateTime;

import com.online_store.backend.api.company.entity.CompanyStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyResponseDto {
    private Long id;

    private String name;

    private Long logo;

    private String description;

    private String websiteUrl;

    private String phone;

    private String email;

    private String address;

    private String taxId;

    private String taxOffice;

    private CompanyStatus status;

    private String rejectionReason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
