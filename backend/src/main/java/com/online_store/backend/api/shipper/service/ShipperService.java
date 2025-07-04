package com.online_store.backend.api.shipper.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.shipper.dto.request.ShipperRequestDto;
import com.online_store.backend.api.shipper.dto.response.ShipperResponseDto;
import com.online_store.backend.api.shipper.entity.Shipper;
import com.online_store.backend.api.shipper.repository.ShipperRepository;
import com.online_store.backend.api.shipper.utils.ShipperUtilsService;
import com.online_store.backend.api.upload.entity.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShipperService {
    private final ShipperRepository shipperRepository;
    private final CommonUtilsService commonUtilsService;
    private final ShipperUtilsService shipperUtilsService;
    private final UploadService uploadService;

    @Transactional
    public void createShipper(ShipperRequestDto shipperRequestDto, MultipartFile file) {
        commonUtilsService.checkImageFileType(file);
        if (shipperRepository.findByName(shipperRequestDto.getName()).isPresent()) {
            throw new RuntimeException("Shipper name already use!");
        }

        Upload upload = uploadService.createFile(file);
        
        Shipper shipper = Shipper.builder()
                .name(shipperRequestDto.getName())
                .logo(upload)
                .description(shipperRequestDto.getDescription())
                .websiteUrl(shipperRequestDto.getWebsiteUrl())
                .phone(shipperRequestDto.getPhone())
                .email(shipperRequestDto.getEmail())
                .address(shipperRequestDto.getAddress())
                .apiKey(shipperUtilsService.generateUniqueApiKey())
                .build();

        shipperRepository.save(shipper);
    }

    public List<ShipperResponseDto> getAllShipper() {
        List<Shipper> shippers = shipperRepository.findAll();
        return shippers.stream().map(shipperUtilsService::mapShipperTorResponseDto).toList();
    }

}
