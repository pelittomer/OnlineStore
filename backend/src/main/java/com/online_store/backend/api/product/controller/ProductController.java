package com.online_store.backend.api.product.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.online_store.backend.api.product.dto.request.ProductRequestDto;
import com.online_store.backend.api.product.dto.response.ProductResponseDto;
import com.online_store.backend.api.product.service.ProductService;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<ApiResponse<String>> createProduct(
            @Valid @RequestPart("product") ProductRequestDto productRequestDto,
            MultipartHttpServletRequest request) {
        productService.createProduct(productRequestDto, request);
        System.out.println(request.getFileMap());
        return ResponseEntity.ok(ApiResponse.success("Product created successfully."));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> fetchProduct() {
        return ResponseEntity.ok(ApiResponse.success(
                "Product found.",
                productService.getProducts()));
    }

}
