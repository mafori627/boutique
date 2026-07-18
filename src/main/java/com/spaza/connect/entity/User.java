package com.spaza.connect.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Entity 
@Table(name = "users") 
@Data
public class User {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; 

    @Column(nullable = false)
    private String password;

    // Added: Unique, mandatory email address field for notifications
    @Column(unique = true, nullable = false)
    private String email;

    private String shopName;
    private String clusterLocation; // e.g., Khayelitsha, Soweto

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
