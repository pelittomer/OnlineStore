package com.online_store.backend.api.brand.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandResponseDto {
    private Long id;
    private String name;
    private Long logo;
}
