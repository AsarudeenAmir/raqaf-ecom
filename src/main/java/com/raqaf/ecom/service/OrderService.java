package com.raqaf.ecom.service;


import com.raqaf.ecom.entity.*;
import com.raqaf.ecom.exception.OrderNotFoundException;
import com.raqaf.ecom.repository.CartItemRepository;
import com.raqaf.ecom.repository.OrderRepository;
import com.raqaf.ecom.repository.ProductRepository;
import com.raqaf.ecom.repository.UserRepository;
import com.raqaf.ecom.service.orderstate.OrderStateContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderStateContext orderStateContext;

    @Transactional
    public Order placeOrder(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot place an order with an empty cart.");
        }

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PLACED)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }

            // Deduct stock - @Version on Product protects against concurrent oversell
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .priceAtPurchase(product.getPrice())
                    .build();

            order.getItems().add(orderItem);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);

        // Clear the cart after successful checkout
        cartItemRepository.deleteByUserId(user.getId());

        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return orderRepository.findByUserId(user.getId());
    }

    // CONCEPT: State pattern in action - we don't decide the next status
    // here, we delegate to whichever OrderState matches the CURRENT status
    @Transactional
    public Order advanceOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        orderStateContext.getState(order.getStatus()).next(order);
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        orderStateContext.getState(order.getStatus()).cancel(order);
        return orderRepository.save(order);
    }
}

