package com.online_store.backend.api.product.entity.embeddables;

import com.online_store.backend.api.variation.entity.Variation;
import com.online_store.backend.api.variation.entity.VariationOption;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockVariation {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variation_id", nullable = false)
    private Variation variation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variation_option_id", nullable = false)
    private VariationOption variationOption;
}
