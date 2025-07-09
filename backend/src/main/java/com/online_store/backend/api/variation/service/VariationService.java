package com.online_store.backend.api.variation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_store.backend.api.category.entity.Category;
import com.online_store.backend.api.category.service.CategoryService;
import com.online_store.backend.api.variation.dto.request.VariationRequestDto;
import com.online_store.backend.api.variation.dto.response.VariationResponseDto;
import com.online_store.backend.api.variation.entity.Variation;
import com.online_store.backend.api.variation.entity.VariationOption;
import com.online_store.backend.api.variation.repository.VariationRepository;
import com.online_store.backend.api.variation.utils.VariationMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VariationService {
        private final VariationRepository variationRepository;
        private final CategoryService categoryService;
        private final VariationMapper variationMapper;

        @Transactional
        public void createVariation(VariationRequestDto variationRequestDto) {
                Category category = getCategoryById(variationRequestDto.getCategory());

                Variation variation = variationMapper.toEntity(variationRequestDto, category);

                Set<VariationOption> variationOptions = variationRequestDto.getOptions().stream()
                                .map(optionName -> VariationOption.builder()
                                                .name(optionName)
                                                .variation(variation)
                                                .build())
                                .collect(Collectors.toSet());

                variation.setVariationOptions(variationOptions);

                variationRepository.save(variation);
        }

        public List<VariationResponseDto> getVariation(Long categoryId) {
                List<Variation> variationsWithCategory;
                List<Variation> allRelevantVariations = new ArrayList<>();

                if (categoryId != null) {
                        Category category = categoryService.findCategoryById(categoryId);
                        variationsWithCategory = variationRepository.findByCategory(category);
                        allRelevantVariations.addAll(variationsWithCategory);
                }

                List<Variation> variationsWithoutCategory = variationRepository.findByCategoryIsNull();

                allRelevantVariations.addAll(variationsWithoutCategory);

                return allRelevantVariations.stream()
                                .map(variationMapper::toDto)
                                .collect(Collectors.toList());
        }

        private Category getCategoryById(Long categoryId) {
                return (categoryId != null) ? categoryService.findCategoryById(categoryId) : null;
        }
}
