package com.spaza.connect.controller;

import com.spaza.connect.dto.Response;
import com.spaza.connect.dto.UserUpdateDTO;
import com.spaza.connect.entity.User;
import com.spaza.connect.exception.ResourceNotFoundException;
import com.spaza.connect.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. READ ALL: Fetch every registered spaza shop profile
    @GetMapping
    public Response<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        // Scrub passwords for network safety
        users.forEach(user -> user.setPassword(null));
        return Response.success(users, "All user profiles retrieved successfully.");
    }

    // 2. READ ONE: Look up a specific profile by database primary key ID
    @GetMapping("/{id}")
    public Response<User> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        user.setPassword(null);
        return Response.success(user, "User profile details loaded successfully.");
    }

    // 3. UPDATE: Modify store profiles, email, or password data securely
    @PutMapping("/{id}")
    public Response<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        // Update with clean validation mapping and re-hash password strings safely
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setShopName(dto.getShopName());
        user.setClusterLocation(dto.getClusterLocation());

        User updatedUser = userRepository.save(user);
        updatedUser.setPassword(null); // Scrub post-save display transparency
        return Response.success(updatedUser, "User profile updated successfully.");
    }

    // 4. DELETE: Completely remove user records index mapping
    @DeleteMapping("/{id}")
    public Response<Void> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        userRepository.delete(user);
        return Response.success(null, "User account profile wiped from index registry successfully.");
    }
}
