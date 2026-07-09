package com.spaza.connect.controller;

import com.spaza.connect.dto.RegisterRequest;
import com.spaza.connect.dto.Response;
import com.spaza.connect.entity.User;
import com.spaza.connect.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Response<String> register(@Valid @RequestBody RegisterRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            return Response.error("Username is already taken");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // BCrypt
        user.setShopName(request.getShopName());
        user.setClusterLocation(request.getClusterLocation());
        userRepository.save(user);
        return Response.success(null, "Registration successful!");
    }
}