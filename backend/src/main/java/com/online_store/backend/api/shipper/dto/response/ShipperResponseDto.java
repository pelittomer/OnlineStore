package com.online_store.backend.api.shipper.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShipperResponseDto {
    private Long id;

    private String name;

    private Long logo;

    private String description;

    private String websiteUrl;

    private String phone;

    private String email;

    private String address;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
