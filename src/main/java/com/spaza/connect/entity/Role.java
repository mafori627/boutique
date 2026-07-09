package com.spaza.connect.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity @Table(name = "roles") @Data
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name; // e.g., "ROLE_SPAZA_OWNER", "ROLE_ADMIN"
}
