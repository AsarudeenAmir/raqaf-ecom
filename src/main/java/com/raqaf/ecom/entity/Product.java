package com.raqaf.ecom.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * CONCEPT: @ManyToOne relationship (many Products belong to one Category).
 * CONCEPT: @Version field for Optimistic Locking - prevents two customers
 * from both "successfully" buying the last item in stock at the same time.
 * When two transactions try to update the same row, whoever commits second
 * gets an OptimisticLockException and must retry. This is a great senior-level
 * interview topic ("how do you prevent overselling in an e-commerce app?").
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Builder.Default
    private int stockQuantity = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Version
    private Long version; // optimistic locking
}