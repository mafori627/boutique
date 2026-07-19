package com.spaza.connect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Please provide a valid email format (e.g., owner@spaza.co.za)")
    private String email;

    @NotBlank(message = "Shop name cannot be blank")
    private String shopName;

    @NotBlank(message = "Cluster location is required")
    private String clusterLocation;

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }

    public String getClusterLocation() { return clusterLocation; }
    public void setClusterLocation(String clusterLocation) { this.clusterLocation = clusterLocation; }
}
