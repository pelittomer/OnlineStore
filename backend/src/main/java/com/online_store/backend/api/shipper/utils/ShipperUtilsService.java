package com.online_store.backend.api.shipper.utils;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.shipper.dto.response.ShipperResponseDto;
import com.online_store.backend.api.shipper.entity.Shipper;
import com.online_store.backend.api.shipper.repository.ShipperRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShipperUtilsService {
    private final ShipperRepository shipperRepository;

    public String generateUniqueApiKey() {
        String apiKey;
        do {
            apiKey = UUID.randomUUID().toString().replace("-", "");
        } while (shipperRepository.findByApiKey(apiKey).isPresent());
        return apiKey;
    }

    public ShipperResponseDto mapShipperTorResponseDto(Shipper shipper) {
        return ShipperResponseDto.builder()
                .id(shipper.getId())
                .name(shipper.getName())
                .logo(shipper.getLogo().getId())
                .description(shipper.getDescription())
                .websiteUrl(shipper.getWebsiteUrl())
                .phone(shipper.getPhone())
                .email(shipper.getEmail())
                .address(shipper.getAddress())
                .isActive(shipper.getIsActive())
                .createdAt(shipper.getCreatedAt())
                .updatedAt(shipper.getUpdatedAt())
                .build();
    }
}
