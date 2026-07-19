package com.spaza.connect.repository;
import com.spaza.connect.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Standard CRUD operations are automatically provided by JpaRepository
}
