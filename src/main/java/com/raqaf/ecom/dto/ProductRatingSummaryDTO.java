package com.raqaf.ecom.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRatingSummaryDTO {
    private Long productId;
    private double averageRating;
    private long totalReviews;
    private List<ReviewDTO> reviews;
}
