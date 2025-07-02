package com.online_store.backend.api.user.auth.utils;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.online_store.backend.api.user.auth.exception.UserAlreadyExistsException;
import com.online_store.backend.api.user.entity.Role;
import com.online_store.backend.api.user.entity.User;
import com.online_store.backend.api.user.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthServiceUtils {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private Integer refreshExpiration;

    public Role mapRoleParamToEnum(Role role) {
        return role == Role.SELLER ? Role.SELLER : Role.CUSTOMER;
    }

    public void validateNewUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists.");
        }
    }

    public String hashedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void addRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("jwt", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(refreshExpiration);
        response.addCookie(refreshTokenCookie);
    }
}
