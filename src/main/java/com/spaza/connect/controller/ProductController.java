package com.spaza.connect.controller;

import com.spaza.connect.dto.ProductDTO;
import com.spaza.connect.dto.Response;
import com.spaza.connect.entity.Product;
import com.spaza.connect.exception.ResourceNotFoundException;
import com.spaza.connect.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // 1. CREATE: Add a new wholesale item to the catalog
    @PostMapping
    public Response<Product> createProduct(@Valid @RequestBody ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setSingleUnitPrice(dto.getSingleUnitPrice());
        product.setBulkUnitPrice(dto.getBulkUnitPrice());
        product.setBulkThresholdQty(dto.getBulkThresholdQty());
        
        Product savedProduct = productRepository.save(product);
        return Response.success(savedProduct, "Product added to wholesale catalog successfully.");
    }

    // 2. READ ALL: Fetch the full wholesaler product directory
    @GetMapping
    public Response<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return Response.success(products, "Fetched wholesaler catalog successfully.");
    }

    // 3. READ ONE: Fetch a specific item details by ID
    @GetMapping("/{id}")
    public Response<Product> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return Response.success(product, "Product details retrieved successfully.");
    }

    // 4. UPDATE: Modify an existing item's metrics or wholesale prices
    @PutMapping("/{id}")
    public Response<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setSingleUnitPrice(dto.getSingleUnitPrice());
        product.setBulkUnitPrice(dto.getBulkUnitPrice());
        product.setBulkThresholdQty(dto.getBulkThresholdQty());
        
        Product updatedProduct = productRepository.save(product);
        return Response.success(updatedProduct, "Product updated successfully.");
    }

    // 5. DELETE: Remove an item entirely from the wholesale records index
    @DeleteMapping("/{id}")
    public Response<Void> deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        
        productRepository.delete(product);
        return Response.success(null, "Product deleted from catalog successfully.");
    }
}
