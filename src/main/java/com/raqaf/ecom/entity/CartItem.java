package com.raqaf.ecom.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * CONCEPT: Each row = one product a user has added to their cart.
 * We link directly to User + Product rather than having a separate
 * "Cart" table, since each user effectively has exactly one active cart.
 */
@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Builder.Default
    private int quantity = 1;
}
