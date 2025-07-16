package com.online_store.backend.api.product.dto.request;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockRequestDto {
    private Integer stockQuantity;

    private BigDecimal additionalPrica;

    @Builder.Default
    private Boolean isLimited = false;

    private Integer replenishQuantity;

    @Valid
    @Builder.Default
    private Set<StockVariationRequestDto> stockVariations = new HashSet<>();
}
