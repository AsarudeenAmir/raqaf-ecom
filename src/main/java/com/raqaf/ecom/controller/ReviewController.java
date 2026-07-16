package com.raqaf.ecom.controller;

import com.raqaf.ecom.dto.ProductRatingSummaryDTO;
import com.raqaf.ecom.dto.ReviewDTO;
import com.raqaf.ecom.dto.ReviewRequest;
import com.raqaf.ecom.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> addReview(@Valid @RequestBody ReviewRequest request,
                                               Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.addReview(authentication.getName(), request));
    }

    // Public - anyone can see a product's reviews and average rating
    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductRatingSummaryDTO> getProductReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getProductRatingSummary(productId));
    }
}