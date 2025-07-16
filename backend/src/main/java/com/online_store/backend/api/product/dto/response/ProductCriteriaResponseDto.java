package com.online_store.backend.api.product.dto.response;

import java.util.HashSet;
import java.util.Set;

import com.online_store.backend.api.variation.dto.response.VariationResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCriteriaResponseDto {
    private Long id;

    private VariationResponseDto variation;

    @Builder.Default
    private Set<CriteriaOptionsResponseDto> options = new HashSet<>();
}
