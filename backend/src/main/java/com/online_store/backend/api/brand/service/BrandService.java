package com.online_store.backend.api.brand.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.brand.dto.request.BrandRequestDto;
import com.online_store.backend.api.brand.dto.response.BrandResponseDto;
import com.online_store.backend.api.brand.entity.Brand;
import com.online_store.backend.api.brand.repository.BrandRepository;
import com.online_store.backend.api.upload.entity.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final CommonUtilsService commonUtilsService;
    private final UploadService uploadService;

    @Transactional
    public void createBrand(BrandRequestDto brandRequestDto, MultipartFile file) {
        commonUtilsService.checkImageFileType(file);
        if (brandRepository.findByName(brandRequestDto.getName()).isPresent()) {
            throw new RuntimeException("Brand with name '" + brandRequestDto.getName() + "' already exists.");
        }

        Upload upload = uploadService.createFile(file);

        Brand brand = Brand.builder()
                .name(brandRequestDto.getName())
                .logo(upload)
                .build();

        brandRepository.save(brand);
    }

    public List<BrandResponseDto> getBrands() {
        return brandRepository.findAll().stream()
                .map(this::mapBrandorResponseDto).toList();
    }

    private BrandResponseDto mapBrandorResponseDto(Brand brand) {
        return BrandResponseDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .logo(brand.getLogo().getId())
                .build();
    }

}
