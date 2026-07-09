package com.spring.boutique.backend.service;

import com.spring.boutique.backend.common.exception.ApiException;
import com.spring.boutique.backend.common.exception.ResourceNotFoundException;
import com.spring.boutique.backend.dto.admin.UserResponse;
import com.spring.boutique.backend.entity.Role;
import com.spring.boutique.backend.entity.User;
import com.spring.boutique.backend.repository.RoleRepository;
import com.spring.boutique.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toResponse);
    }

    public UserResponse promoteToAdmin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with email: " + email));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new ApiException("ROLE_ADMIN not found - check DataSeeder ran on startup", HttpStatus.INTERNAL_SERVER_ERROR));

        if (user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
            throw new ApiException(user.getEmail() + " is already an admin", HttpStatus.CONFLICT);
        }

        user.getRoles().add(adminRole);
        userRepository.save(user);

        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        List<String> roleNames = user.getRoles().stream().map(Role::getName).toList();
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                roleNames,
                user.getCreatedAt()
        );
    }
}
