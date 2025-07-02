package com.online_store.backend.api.upload.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.upload.entity.Upload;

public interface UploadRepository extends JpaRepository<Upload, Long> {

}
