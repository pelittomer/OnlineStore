package com.online_store.backend.api.product.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.online_store.backend.api.product.entity.embeddables.StockVariation;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productStock")
public class ProductStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer stockQuantity;

    private BigDecimal additionalPrice;

    private Boolean isLimited;

    private Integer replenishQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Product product;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_stock_variation", joinColumns = @JoinColumn(name = "product_stock_id"))
    @Builder.Default
    private Set<StockVariation> stockVariations = new HashSet<>();
}