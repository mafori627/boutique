package com.spring.boutique.backend.controller;

import com.spring.boutique.backend.common.Response;
import com.spring.boutique.backend.dto.product.ProductRequest;
import com.spring.boutique.backend.dto.product.ProductResponse;
import com.spring.boutique.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Examples:
    //   GET /api/products?page=0&size=12
    //   GET /api/products?search=dress&page=0&size=12&sort=price,asc
    //   GET /api/products?categoryId=3
    @GetMapping
    public ResponseEntity<Response<Page<ProductResponse>>> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(size = 12, sort = "id") Pageable pageable) {

        Page<ProductResponse> products = productService.getProducts(search, categoryId, pageable);
        return ResponseEntity.ok(Response.success(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ProductResponse>> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(Response.success(productService.getProductById(id)));
    }

    @PostMapping
    public ResponseEntity<Response<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse created = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success("Product created", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<ProductResponse>> updateProduct(
            @PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        ProductResponse updated = productService.updateProduct(id, request);
        return ResponseEntity.ok(Response.success("Product updated", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(Response.success("Product deleted", null));
    }
}
