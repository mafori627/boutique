package com.spaza.connect.repository;
import com.spaza.connect.entity.BuyingPool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BuyingPoolRepository extends JpaRepository<BuyingPool, Long> {
    
    // For pagination and filtering by area cluster
    Page<BuyingPool> findByClusterLocation(String clusterLocation, Pageable pageable);
    
    // For locating an existing open pool to join
    Optional<BuyingPool> findByClusterLocationAndProductIdAndStatus(
            String clusterLocation, Long productId, String status);
}
