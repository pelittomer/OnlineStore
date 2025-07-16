package com.online_store.backend.api.product.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.online_store.backend.api.brand.dto.response.BrandResponseDto;
import com.online_store.backend.api.category.dto.response.CategoryResponseDto;
import com.online_store.backend.api.company.dto.response.CompanyResponseDto;
import com.online_store.backend.api.product.entity.embeddables.Discount;
import com.online_store.backend.api.shipper.dto.response.ShipperResponseDto;

import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String name;
    private BigDecimal price;

    @Embedded
    private Discount discount;

    private Boolean isPublished;

    @Builder.Default
    private List<Long> images = new ArrayList<>();

    private BrandResponseDto brand;

    private ShipperResponseDto shipper;

    private CompanyResponseDto company;

    private CategoryResponseDto category;

    private ProductDetailResponseDto productDetail;

    @Builder.Default
    private List<ProductStockResponseDto> productStock = new ArrayList<>();

    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
