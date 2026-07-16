package com.raqaf.ecom.service;


import com.raqaf.ecom.dto.ProductRatingSummaryDTO;
import com.raqaf.ecom.dto.ReviewDTO;
import com.raqaf.ecom.dto.ReviewRequest;
import com.raqaf.ecom.entity.Product;
import com.raqaf.ecom.entity.Review;
import com.raqaf.ecom.entity.User;
import com.raqaf.ecom.exception.ProductNotFoundException;
import com.raqaf.ecom.exception.ReviewAlreadyExistsException;
import com.raqaf.ecom.repository.ProductRepository;
import com.raqaf.ecom.repository.ReviewRepository;
import com.raqaf.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewDTO addReview(String userEmail, ReviewRequest request) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + request.getProductId()));

        // CONCEPT: one review per user per product - checked via Optional
        reviewRepository.findByProductIdAndUserId(product.getId(), user.getId())
                .ifPresent(r -> {
                    throw new ReviewAlreadyExistsException("You have already reviewed this product.");
                });

        Review review = Review.builder()
                .product(product)
                .user(user)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        return toDTO(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public ProductRatingSummaryDTO getProductRatingSummary(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);

        // CONCEPT: Java Streams - mapToInt + average() instead of a manual
        // loop with a running sum and counter. orElse(0.0) handles the
        // "no reviews yet" case (average() returns an OptionalDouble).
        double average = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return ProductRatingSummaryDTO.builder()
                .productId(productId)
                .averageRating(Math.round(average * 10.0) / 10.0) // round to 1 decimal
                .totalReviews(reviews.size())
                .reviews(reviewDTOs)
                .build();
    }

    private ReviewDTO toDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .productId(review.getProduct().getId())
                .reviewerName(review.getUser().getName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
