package com.spring.boutique.backend.dto.order;

import com.spring.boutique.backend.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String customerEmail;
    private OrderStatus status;
    private Instant orderDate;
    private BigDecimal totalAmount;
    private String deliveryAddress;
    private List<OrderItemResponse> items;
}
