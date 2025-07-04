package com.online_store.backend.api.company.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.online_store.backend.api.upload.entity.Upload;
import com.online_store.backend.api.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Upload logo;

    @Column(length = 1000)
    private String description;

    @Column(length = 255)
    private String websiteUrl;

    @Column(length = 11)
    private String phone;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 255)
    private String address;

    @Column(nullable = false, unique = true, length = 20)
    private String taxId;

    @Column(length = 100)
    private String taxOffice;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private CompanyStatus status = CompanyStatus.Pending;

    @Column(length = 500)
    private String rejectionReason;

    @OneToOne(mappedBy = "company", fetch = FetchType.LAZY)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
