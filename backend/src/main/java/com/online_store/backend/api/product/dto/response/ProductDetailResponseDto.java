package com.online_store.backend.api.product.dto.response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.online_store.backend.api.product.dto.base.FeatureDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponseDto {
    private Long id;

    private String description;

    private String shortDescription;

    @Builder.Default
    private Set<FeatureDto> features = new HashSet<>();

    @Builder.Default
    private List<ProductCriteriaResponseDto> criteria = new ArrayList<>();
}
