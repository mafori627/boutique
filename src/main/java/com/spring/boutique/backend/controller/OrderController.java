package com.spring.boutique.backend.controller;

import com.spring.boutique.backend.common.Response;
import com.spring.boutique.backend.dto.order.OrderRequest;
import com.spring.boutique.backend.dto.order.OrderResponse;
import com.spring.boutique.backend.dto.order.OrderStatusUpdateRequest;
import com.spring.boutique.backend.entity.User;
import com.spring.boutique.backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Place an order. Requires a valid JWT (any logged-in user).
    @PostMapping
    public ResponseEntity<Response<OrderResponse>> createOrder(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody OrderRequest request) {

        OrderResponse created = orderService.createOrder(currentUser.getEmail(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success("Order placed successfully", created));
    }

    // The logged-in user's own order history.
    @GetMapping("/my")
    public ResponseEntity<Response<Page<OrderResponse>>> getMyOrders(
            @AuthenticationPrincipal User currentUser,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        Page<OrderResponse> orders = orderService.getMyOrders(currentUser.getEmail(), pageable);
        return ResponseEntity.ok(Response.success(orders));
    }

    // Single order detail. Owner or admin only (enforced in the service).
    @GetMapping("/{id}")
    public ResponseEntity<Response<OrderResponse>> getOrder(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id) {

        OrderResponse order = orderService.getOrderById(id, currentUser);
        return ResponseEntity.ok(Response.success(order));
    }

    // Admin: every order in the system, paginated.
    @GetMapping("/all")
    public ResponseEntity<Response<Page<OrderResponse>>> getAllOrders(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {

        Page<OrderResponse> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(Response.success(orders));
    }

    // Admin: move an order through its lifecycle (PENDING -> PAID -> SHIPPED -> DELIVERED, or CANCELLED).
    @PutMapping("/{id}/status")
    public ResponseEntity<Response<OrderResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateRequest request) {

        OrderResponse updated = orderService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok(Response.success("Order status updated", updated));
    }
}
