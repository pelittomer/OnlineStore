package com.online_store.backend.api.profile.dto.request;

import java.time.LocalDate;

import com.online_store.backend.api.profile.entity.Gender;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequestDto {
    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotNull(message = "Birth of date cannot be null")
    @Past(message = "Birth of date must be in the past")
    private LocalDate birthOfDate;

    @Builder.Default
    private Gender gender = Gender.PREFER_NOT_TO_SAY;
}
