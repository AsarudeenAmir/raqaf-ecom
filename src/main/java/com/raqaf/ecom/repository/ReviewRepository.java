package com.raqaf.ecom.repository;

import com.raqaf.ecom.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);
    Optional<Review> findByProductIdAndUserId(Long productId, Long userId);
}