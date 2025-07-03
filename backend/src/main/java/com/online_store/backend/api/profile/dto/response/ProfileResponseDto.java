package com.online_store.backend.api.profile.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.online_store.backend.api.profile.entity.Gender;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthOfDate;
    private Gender gender;
    private Long avatar;
    private LocalDateTime createdAt;
}
