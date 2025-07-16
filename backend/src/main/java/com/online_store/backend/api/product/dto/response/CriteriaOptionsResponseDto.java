package com.online_store.backend.api.product.dto.response;

import java.util.HashSet;
import java.util.Set;

import com.online_store.backend.api.variation.dto.response.VariationOptionResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaOptionsResponseDto {
    private Long id;
    private VariationOptionResponseDto option;

    @Builder.Default
    private Set<Long> images = new HashSet<>();
}
