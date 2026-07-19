package com.spaza.connect.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity @Table(name = "products") @Data
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private BigDecimal singleUnitPrice;
    private BigDecimal bulkUnitPrice;
    private Integer bulkThresholdQty;
}