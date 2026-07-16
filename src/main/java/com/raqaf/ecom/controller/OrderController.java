package com.raqaf.ecom.controller;


import com.raqaf.ecom.entity.Order;
import com.raqaf.ecom.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Checkout - converts the logged-in user's cart into an Order
    @PostMapping
    public ResponseEntity<Order> placeOrder(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.placeOrder(authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {
        return ResponseEntity.ok(orderService.getOrdersForUser(authentication.getName()));
    }

    // Admin action - moves order to its next status (PLACED -> CONFIRMED -> SHIPPED -> DELIVERED)
    @PutMapping("/{orderId}/advance")
    public ResponseEntity<Order> advanceOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.advanceOrder(orderId));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }
}
