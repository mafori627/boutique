package com.spaza.connect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Please provide a valid email format")
    private String email;

    @NotBlank(message = "Shop name cannot be blank")
    private String shopName;

    @NotBlank(message = "Cluster location is required")
    private String clusterLocation;

    // Getters and Setters
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }

    public String getClusterLocation() { return clusterLocation; }
    public void setClusterLocation(String clusterLocation) { this.clusterLocation = clusterLocation; }
}
