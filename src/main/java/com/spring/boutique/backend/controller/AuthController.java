package com.spring.boutique.backend.controller;

import com.spring.boutique.backend.common.Response;
import com.spring.boutique.backend.dto.auth.AuthResponse;
import com.spring.boutique.backend.dto.auth.LoginRequest;
import com.spring.boutique.backend.dto.auth.RegisterRequest;
import com.spring.boutique.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse result = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success("Account created successfully", result));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse result = authService.login(request);
        return ResponseEntity.ok(Response.success("Login successful", result));
    }
}
