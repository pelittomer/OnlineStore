package com.online_store.backend.api.product.dto.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    private String name;

    private BigDecimal price;

    @Builder.Default
    private Boolean isPublished = false;

    private Long brand;

    private Long shipper;

    private Long category;

    @Valid
    private ProductDetailRequestDto productDetail;

    @Valid
    @Builder.Default
    private List<ProductStockRequestDto> productStock = new ArrayList<>();
}
