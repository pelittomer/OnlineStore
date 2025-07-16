package com.online_store.backend.api.brand.utils;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.brand.dto.response.BrandResponseDto;
import com.online_store.backend.api.brand.entity.Brand;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandUtilsService {

    public BrandResponseDto mapBrandorResponseDto(Brand brand) {
        return BrandResponseDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .logo(brand.getLogo().getId())
                .build();
    }
}
