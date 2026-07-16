package com.raqaf.ecom.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long id;
    private Long productId;
    private String reviewerName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}