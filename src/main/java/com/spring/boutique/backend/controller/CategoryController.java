package com.spring.boutique.backend.controller;

import com.spring.boutique.backend.common.Response;
import com.spring.boutique.backend.entity.Category;
import com.spring.boutique.backend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Response<List<Category>>> getAll() {
        return ResponseEntity.ok(Response.success(categoryService.getAllCategories()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Category>> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(Response.success(categoryService.getCategoryById(id)));
    }

    @PostMapping
    public ResponseEntity<Response<Category>> create(@Valid @RequestBody Category category) {
        Category created = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Category created", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<Category>> update(@PathVariable Long id, @Valid @RequestBody Category category) {
        return ResponseEntity.ok(Response.success("Category updated", categoryService.updateCategory(id, category)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(Response.success("Category deleted", null));
    }
}
