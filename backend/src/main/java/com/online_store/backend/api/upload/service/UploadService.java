package com.online_store.backend.api.upload.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.upload.entity.Upload;
import com.online_store.backend.api.upload.repository.UploadRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final UploadRepository uploadRepository;

    public Upload createFile(MultipartFile file) {
        try {
            Upload upload = Upload.builder()
                    .fileContent(file.getBytes())
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .build();
            return uploadRepository.save(upload);
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename());
        }
    }

}
