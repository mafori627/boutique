package com.spaza.connect.controller;

import com.spaza.connect.dto.LoginRequest;
import com.spaza.connect.dto.RegisterRequest;
import com.spaza.connect.dto.Response;
import com.spaza.connect.entity.Role;
import com.spaza.connect.entity.User;
import com.spaza.connect.repository.RoleRepository;
import com.spaza.connect.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;



@RestController 
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository; // Injected
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Response<String> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return Response.error("Username is already taken");
        }

        // 1. Fetch the default ROLE_USER from your MySQL roles table
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    // Fallback to auto-create role if it is somehow missing from the database records
                    Role role = new Role();
                    role.setName("ROLE_USER");
                    return roleRepository.save(role);
                });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setShopName(request.getShopName());
        user.setClusterLocation(request.getClusterLocation());
        
        // 2. Assign the fetched role entity to the user's role collection list
        user.getRoles().add(defaultRole);

        userRepository.save(user);
        return Response.success(null, "Registration successful!");
    }

    @PostMapping("/login")
    public Response<User> login(@Valid @RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return Response.error("Invalid username or password");
        }

        user.setPassword(null);
        return Response.success(user, "Login successful!");
    }
}
