package com.online_store.backend.api.product.dto.request;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.online_store.backend.api.product.dto.base.FeatureDto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailRequestDto {
    private String description;

    private String shortDescription;

    @Valid
    @Builder.Default
    private Set<FeatureDto> features = new HashSet<>();

    @Valid
    @Builder.Default
    private List<ProductCriteriaRequestDto> criteria = new ArrayList<>();
}