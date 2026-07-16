package com.raqaf.ecom.entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * CONCEPT: We store `priceAtPurchase` here instead of relying on
 * product.getPrice() later. Why? If the product's price changes next
 * week, past orders should still show what the customer actually paid -
 * a very common real-world requirement interviewers like to probe.
 */
@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;

    private BigDecimal priceAtPurchase;
}
