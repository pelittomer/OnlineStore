package com.online_store.backend.api.product.dto.request;

import java.util.ArrayList;
import java.util.List;

import com.online_store.backend.api.product.dto.response.CriteriaOptionsResponseDto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCriteriaRequestDto {
    private Long variation;

    @Valid
    @Builder.Default
    private List<CriteriaOptionsResponseDto> options = new ArrayList<>();
}
