package com.spaza.connect.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "buying_pools") @Data
public class BuyingPool {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clusterLocation;
    private Integer currentTotalQty = 0;
    private String status = "OPEN"; // OPEN, LOCKED

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "buyingPool", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PoolOrder> orders = new ArrayList<>();
}
