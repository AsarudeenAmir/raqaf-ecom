package com.raqaf.ecom.controller;


import com.raqaf.ecom.dto.AddToCartRequest;
import com.raqaf.ecom.dto.CartItemDTO;
import com.raqaf.ecom.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONCEPT: `Authentication` is injected automatically by Spring Security -
 * it holds the currently logged-in user (extracted from the JWT by
 * JwtAuthFilter). authentication.getName() gives us the email, so we
 * never need the client to tell us "who they are" in the request body -
 * that would be a security hole.
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartItemDTO> addToCart(@Valid @RequestBody AddToCartRequest request,
                                                 Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addToCart(authentication.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getCart(Authentication authentication) {
        return ResponseEntity.ok(cartService.getCart(authentication.getName()));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long cartItemId) {
        cartService.removeFromCart(cartItemId);
        return ResponseEntity.noContent().build();
    }
}

