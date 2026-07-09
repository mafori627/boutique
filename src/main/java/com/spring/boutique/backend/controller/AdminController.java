package com.spring.boutique.backend.controller;

import com.spring.boutique.backend.common.Response;
import com.spring.boutique.backend.dto.admin.PromoteUserRequest;
import com.spring.boutique.backend.dto.admin.UserResponse;
import com.spring.boutique.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Every endpoint here is admin-only - see SecurityConfig ("/api/admin/**" -> ROLE_ADMIN).
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Response<Page<UserResponse>>> getAllUsers(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(Response.success(userService.getAllUsers(pageable)));
    }

    @PostMapping("/promote")
    public ResponseEntity<Response<UserResponse>> promoteUser(@Valid @RequestBody PromoteUserRequest request) {
        UserResponse promoted = userService.promoteToAdmin(request.getEmail());
        return ResponseEntity.ok(Response.success(promoted.getEmail() + " is now an admin", promoted));
    }
}
