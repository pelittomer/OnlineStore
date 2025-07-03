package com.online_store.backend.api.profile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.online_store.backend.api.profile.dto.request.ProfileRequestDto;
import com.online_store.backend.api.profile.dto.response.ProfileResponseDto;
import com.online_store.backend.api.profile.entity.Profile;
import com.online_store.backend.api.upload.entity.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.api.user.entity.User;
import com.online_store.backend.api.user.repository.UserRepository;
import com.online_store.backend.common.utils.CommonUtilsService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final UploadService uploadService;
    private final CommonUtilsService commonUtilsService;

    @Transactional
    public void createProfile(ProfileRequestDto profileRequestDto, MultipartFile file) {
        commonUtilsService.checkImageFileType(file);
        User currentUser = commonUtilsService.getCurrentUser();

        if (currentUser.getProfile() != null) {
            throw new RuntimeException("Profile already exists!");
        }

        Upload upload = uploadService.createFile(file);

        Profile profile = Profile.builder()
                .avatar(upload)
                .birthOfDate(profileRequestDto.getBirthOfDate())
                .firstName(profileRequestDto.getFirstName())
                .lastName(profileRequestDto.getLastName())
                .gender(profileRequestDto.getGender())
                .build();

        currentUser.setProfile(profile);
        userRepository.save(currentUser);
    }

    @Transactional
    public void updateProfile(ProfileRequestDto profileRequestDto, MultipartFile file) {
        User currentUser = commonUtilsService.getCurrentUser();

        Profile existingProfile = currentUser.getProfile();
        if (existingProfile == null) {
            throw new EntityNotFoundException("User with ID: " + currentUser.getId());
        }

        if (file != null) {
            commonUtilsService.checkImageFileType(file);
            Upload upload = uploadService.createFile(file);
            existingProfile.setAvatar(upload);
        }

        existingProfile.setFirstName(profileRequestDto.getFirstName());
        existingProfile.setLastName(profileRequestDto.getFirstName());
        existingProfile.setBirthOfDate(profileRequestDto.getBirthOfDate());
        existingProfile.setGender(profileRequestDto.getGender());
        userRepository.save(currentUser);
    }

    public ProfileResponseDto getCurrentUserProfile() {
        User currentUser = commonUtilsService.getCurrentUser();

        Profile currentProfile = currentUser.getProfile();

        if (currentProfile == null) {
            throw new EntityExistsException("Profile not found!");
        }

        return ProfileResponseDto.builder()
                .id(currentProfile.getId())
                .avatar(currentProfile.getAvatar().getId())
                .birthOfDate(currentProfile.getBirthOfDate())
                .firstName(currentProfile.getFirstName())
                .lastName(currentProfile.getLastName())
                .createdAt(currentProfile.getCreatedAt())
                .build();
    }

}
