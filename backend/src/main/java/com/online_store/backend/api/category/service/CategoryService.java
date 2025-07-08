package com.online_store.backend.api.category.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.category.dto.request.CategoryRequestDto;
import com.online_store.backend.api.category.dto.response.CategoryResponseDto;
import com.online_store.backend.api.category.entity.Category;
import com.online_store.backend.api.category.repository.CategoryRepository;
import com.online_store.backend.api.upload.entity.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CommonUtilsService commonUtilsService;
    private final UploadService uploadService;

    @Transactional
    public void createCategory(
            CategoryRequestDto categoryRequestDto,
            MultipartFile imageFile,
            MultipartFile iconFile) {
        commonUtilsService.checkImageFileType(imageFile);
        commonUtilsService.checkImageFileType(iconFile);

        Upload imageUpload = uploadService.createFile(imageFile);
        Upload iconUpload = uploadService.createFile(iconFile);

        Category newCategory = Category.builder()
                .name(categoryRequestDto.getName())
                .description(categoryRequestDto.getDescription())
                .image(imageUpload)
                .icon(iconUpload)
                .build();

        if (categoryRequestDto.getParent() == null) {
            Long maxRight = categoryRepository.findMaxRightValue();
            if (maxRight == null) {
                newCategory.setLeftValue(1);
                newCategory.setRightValue(2);
            } else {
                newCategory.setLeftValue(maxRight.intValue() + 1);
                newCategory.setRightValue(maxRight.intValue() + 2);
            }
        } else {
            Category parentCategory = categoryRepository.findById(categoryRequestDto.getParent())
                    .orElseThrow(() -> new RuntimeException(
                            "Parent category not found with ID: " + categoryRequestDto.getParent()));

            categoryRepository.incrementRightValuesGreaterThanOrEqualTo(parentCategory.getRightValue(), 2);
            categoryRepository.incrementLeftValuesGreaterThan(parentCategory.getRightValue(), 2);

            newCategory.setLeftValue(parentCategory.getRightValue());
            newCategory.setRightValue(parentCategory.getRightValue() + 1);
            newCategory.setParent(parentCategory);
        }

        categoryRepository.save(newCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAllByOrderByLeftValueAsc().stream() 
                .map(this::mapCategoryToResponseDto)
                .collect(Collectors.toList());
    }

    private CategoryResponseDto mapCategoryToResponseDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .image(category.getImage() != null ? category.getImage().getId() : null)
                .icon(category.getIcon() != null ? category.getIcon().getId() : null)
                .leftValue(category.getLeftValue())
                .rightValue(category.getRightValue())
                .parent(category.getParent() != null ? category.getParent().getId() : null)
                .build();
    }

}
