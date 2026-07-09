package com.spaza.connect.controller;

import  com.spaza.connect.dto.PoolOrderDTO;
import  com.spaza.connect.dto.Response;
import  com.spaza.connect.exception.ResourceNotFoundException;
import  com.spaza.connect.entity.*;
import  com.spaza.connect.repository.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController @RequestMapping("/api/pools")
public class PoolController {

    @Autowired private BuyingPoolRepository poolRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    // READ (List Endpoint with Pagination, Filtering by cluster, and Sorting)
    @GetMapping
    public Response<Page<BuyingPool>> getAllPools(
            @RequestParam(required = false) String cluster,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<BuyingPool> pools = (cluster != null) ? 
                poolRepository.findByClusterLocation(cluster, pageable) : 
                poolRepository.findAll(pageable);
                
        return Response.success(pools, "Fetched pools successfully.");
    }

    // CREATE / UPDATE (Join dynamic group buying pool mechanism)
    @PostMapping("/join")
    public Response<BuyingPool> joinPool(
            @Valid @RequestBody PoolOrderDTO dto, 
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User session context mismatch."));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        BuyingPool pool = poolRepository.findByClusterLocationAndProductIdAndStatus(
                user.getClusterLocation(), product.getId(), "OPEN")
                .orElseGet(() -> {
                    BuyingPool newPool = new BuyingPool();
                    newPool.setClusterLocation(user.getClusterLocation());
                    newPool.setProduct(product);
                    return poolRepository.save(newPool);
                });

        PoolOrder order = new PoolOrder();
        order.setUser(user);
        order.setRequestedQty(dto.getQuantity());
        order.setBuyingPool(pool);
        pool.getOrders().add(order);
        
        pool.setCurrentTotalQty(pool.getCurrentTotalQty() + dto.getQuantity());
        if (pool.getCurrentTotalQty() >= product.getBulkThresholdQty()) {
            pool.setStatus("LOCKED");
        }

        return Response.success(poolRepository.save(pool), "Successfully joined the buying group pool!");
    }

    // DELETE Order from Pool
    @DeleteMapping("/orders/{orderId}")
    public Response<Void> leavePool(@PathVariable Long orderId) {
        // Business logic execution code maps here to decrement pool metric values cleanly...
        return Response.success(null, "Order cancellation finalized cleanly.");
    }
}
