package com.raqaf.ecom.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * CONCEPT: JPA Entity for authentication + Spring Security integration.
 * The `role` field drives role-based access control (CUSTOMER vs ADMIN)
 * that we'll wire up with Spring Security in a later step.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // will be stored as a BCrypt hash, never plain text

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.CUSTOMER;

    public enum Role {
        CUSTOMER, ADMIN
    }
}
