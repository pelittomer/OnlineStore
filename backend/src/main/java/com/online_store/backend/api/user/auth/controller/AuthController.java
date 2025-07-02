package com.online_store.backend.api.user.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.user.auth.dto.request.AuthRequestDto;
import com.online_store.backend.api.user.auth.service.AuthService;
import com.online_store.backend.api.user.entity.Role;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/user/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("sign-in")
    public ResponseEntity<ApiResponse<String>> authenticateUser(
            @Valid @RequestBody AuthRequestDto authRequestDto,
            HttpServletResponse response) {
        String accessToken = service.authenticate(authRequestDto, response);
        return ResponseEntity.ok(ApiResponse.success("Authentication successful.", accessToken));
    }

    @PostMapping("{role}/sign-up")
    public ResponseEntity<ApiResponse<String>> registerUser(
            @Valid @RequestBody AuthRequestDto authRequestDto,
            @PathVariable Role role) {
        return ResponseEntity.ok(ApiResponse.success(service.registerUser(authRequestDto, role)));
    }

    @GetMapping("refresh")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }

    @PostMapping("sign-out")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletResponse response) {
        return ResponseEntity.ok(ApiResponse.success(service.logout(response)));
    }

}
