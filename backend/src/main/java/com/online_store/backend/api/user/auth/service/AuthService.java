package com.online_store.backend.api.user.auth.service;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online_store.backend.api.user.auth.dto.request.AuthRequestDto;
import com.online_store.backend.api.user.auth.exception.UserAlreadyExistsException;
import com.online_store.backend.api.user.auth.utils.AuthServiceUtils;
import com.online_store.backend.api.user.entity.Role;
import com.online_store.backend.api.user.entity.User;
import com.online_store.backend.api.user.repository.UserRepository;
import com.online_store.backend.common.exception.ErrorResponse;
import com.online_store.backend.modules.jwt.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthServiceUtils authServiceUtils;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public String authenticate(
            AuthRequestDto authRequestDto,
            HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password!");
        }

        User user = userRepository.findByEmail(authRequestDto.getEmail())
                .orElseThrow(
                        () -> new UserAlreadyExistsException("User not found with email:" + authRequestDto.getEmail()));
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        authServiceUtils.addRefreshTokenCookie(refreshToken, response);
        return accessToken;
    }

    public String registerUser(
            AuthRequestDto authRequestDto,
            Role role) {
        Role userRole = authServiceUtils.mapRoleParamToEnum(role);
        authServiceUtils.validateNewUser(authRequestDto.getEmail());

        String hashedPassword = authServiceUtils.hashedPassword(authRequestDto.getPassword());

        User user = User.builder()
                .email(authRequestDto.getEmail())
                .password(hashedPassword)
                .role(userRole)
                .build();
        userRepository.save(user);

        return String.format("User %s registered successfully.", authRequestDto.getEmail());
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail == null || userEmail.trim().isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            if (!jwtService.isTokenValid(refreshToken, userDetails)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            var accessToken = jwtService.generateToken(userDetails);

            response.setContentType("application/json");
            response.setStatus(HttpStatus.OK.value());
            objectMapper.writeValue(response.getOutputStream(), accessToken);

        } catch (UsernameNotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            objectMapper.writeValue(response.getOutputStream(),
                    ErrorResponse.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            objectMapper.writeValue(response.getOutputStream(),
                    ErrorResponse.builder().message("An unexpected error occurred: " + e.getMessage()).build());
        }
    }

    public String logout(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("jwt", "");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(refreshTokenCookie);

        return "You have successfully logged out.";
    }

}
