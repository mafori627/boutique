package com.spring.boutique.backend.service;

import com.spring.boutique.backend.common.exception.ApiException;
import com.spring.boutique.backend.dto.auth.AuthResponse;
import com.spring.boutique.backend.dto.auth.LoginRequest;
import com.spring.boutique.backend.dto.auth.RegisterRequest;
import com.spring.boutique.backend.entity.Role;
import com.spring.boutique.backend.entity.User;
import com.spring.boutique.backend.repository.RoleRepository;
import com.spring.boutique.backend.repository.UserRepository;
import com.spring.boutique.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException("An account with this email already exists", HttpStatus.CONFLICT);
        }

        // Every new signup gets ROLE_CUSTOMER by default.
        // Create ROLE_CUSTOMER / ROLE_ADMIN rows once via a data seed / migration.
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new ApiException("Default role not found - seed roles table first", HttpStatus.INTERNAL_SERVER_ERROR));

        Set<Role> roles = new HashSet<>();
        roles.add(customerRole);

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRoles(roles);

        userRepository.save(user);

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user.getEmail(), user.getFullName());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("Invalid email or password", HttpStatus.UNAUTHORIZED));

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user.getEmail(), user.getFullName());
    }
}
