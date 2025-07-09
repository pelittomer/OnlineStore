package com.online_store.backend.api.variation.utils;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.category.entity.Category;
import com.online_store.backend.api.variation.dto.request.VariationRequestDto;
import com.online_store.backend.api.variation.dto.response.VariationOptionResponseDto;
import com.online_store.backend.api.variation.dto.response.VariationResponseDto;
import com.online_store.backend.api.variation.entity.Variation;
import com.online_store.backend.api.variation.entity.VariationOption;

@Component
public class VariationMapper {

    public Variation toEntity(VariationRequestDto requestDto, Category category) {
        return Variation.builder()
                .name(requestDto.getName())
                .category(category)
                .build();
    }

    public VariationResponseDto toDto(Variation variation) {
        Long categoryIdDto = (variation.getCategory() != null) ? variation.getCategory().getId() : null;
        Set<VariationOptionResponseDto> options = convertOptionsToDto(variation.getVariationOptions());

        return VariationResponseDto.builder()
                .id(variation.getId())
                .name(variation.getName())
                .category(categoryIdDto)
                .options(options)
                .build();
    }

    private Set<VariationOptionResponseDto> convertOptionsToDto(Set<VariationOption> variationOptions) {
        if (variationOptions == null || variationOptions.isEmpty()) {
            return Collections.emptySet();
        }
        return variationOptions.stream()
                .map(this::toOptionDto)
                .collect(Collectors.toSet());
    }

    public VariationOptionResponseDto toOptionDto(VariationOption variationOption) {
        return VariationOptionResponseDto.builder()
                .id(variationOption.getId())
                .name(variationOption.getName())
                .build();
    }
}
