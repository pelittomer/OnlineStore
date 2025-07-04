package com.online_store.backend.api.company.dto.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUpdateRequestDto {
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @URL(message = "Invalid website URL format")
    @Size(max = 255, message = "Website URL cannot exceed 255 characters")
    private String websiteUrl;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number format")
    private String phone;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @Size(max = 100, message = "Tax Office cannot exceed 100 characters")
    private String taxOffice;
}
