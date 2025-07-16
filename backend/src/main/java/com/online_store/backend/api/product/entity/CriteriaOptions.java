package com.online_store.backend.api.product.entity;

import java.util.HashSet;
import java.util.Set;

import com.online_store.backend.api.upload.entity.Upload;
import com.online_store.backend.api.variation.entity.VariationOption;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "criteriaOptions")
public class CriteriaOptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variation_option_id", nullable = false)
    private VariationOption option;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(name = "criteria_option_image", joinColumns = @JoinColumn(name = "criteria_option_id"), inverseJoinColumns = @JoinColumn(name = "upload_id"))
    @Builder.Default
    private Set<Upload> images = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_criteria_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ProductCriteria productCriteria;
}
