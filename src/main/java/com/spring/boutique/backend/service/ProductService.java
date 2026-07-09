package com.spring.boutique.backend.service;

import com.spring.boutique.backend.common.exception.ResourceNotFoundException;
import com.spring.boutique.backend.dto.product.ProductRequest;
import com.spring.boutique.backend.dto.product.ProductResponse;
import com.spring.boutique.backend.entity.Category;
import com.spring.boutique.backend.entity.Product;
import com.spring.boutique.backend.repository.CategoryRepository;
import com.spring.boutique.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // search: optional name filter (partial, case-insensitive)
    // categoryId: optional category filter
    // pageable: carries page number, size, and sort (e.g. ?page=0&size=10&sort=price,asc)
    public Page<ProductResponse> getProducts(String search, Long categoryId, Pageable pageable) {
        Page<Product> products;

        boolean hasSearch = search != null && !search.isBlank();
        boolean hasCategory = categoryId != null;

        if (hasSearch && hasCategory) {
            products = productRepository.findByNameContainingIgnoreCaseAndCategoryId(search, categoryId, pageable);
        } else if (hasSearch) {
            products = productRepository.findByNameContainingIgnoreCase(search, pageable);
        } else if (hasCategory) {
            products = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        return products.map(this::toResponse);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        return toResponse(product);
    }

    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + request.getCategoryId()));

        Product product = new Product();
        applyRequest(product, request, category);

        return toResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + request.getCategoryId()));

        applyRequest(product, request, category);

        return toResponse(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }
        productRepository.deleteById(id);
    }

    private void applyRequest(Product product, ProductRequest request, Category category) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCreatedAt(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }
}
