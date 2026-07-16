package com.raqaf.ecom.service;


import com.raqaf.ecom.dto.AddToCartRequest;
import com.raqaf.ecom.dto.CartItemDTO;
import com.raqaf.ecom.entity.CartItem;
import com.raqaf.ecom.entity.Product;
import com.raqaf.ecom.entity.User;
import com.raqaf.ecom.exception.ProductNotFoundException;
import com.raqaf.ecom.repository.CartItemRepository;
import com.raqaf.ecom.repository.ProductRepository;
import com.raqaf.ecom.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public CartItemDTO addToCart(String userEmail, @Valid  AddToCartRequest request) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + request.getProductId()));

        // If it's already in the cart, just increase the quantity instead of a duplicate row
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(user.getId(), product.getId())
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + request.getQuantity());
                    return existing;
                })
                .orElse(CartItem.builder()
                        .user(user)
                        .product(product)
                        .quantity(request.getQuantity())
                        .build());

        return toDTO(cartItemRepository.save(cartItem));
    }

    @Transactional(readOnly = true)
    public List<CartItemDTO> getCart(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return cartItemRepository.findByUserId(user.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    private CartItemDTO toDTO(CartItem item) {
        return CartItemDTO.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .price(item.getProduct().getPrice())
                .quantity(item.getQuantity())
                .subtotal(item.getProduct().getPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())))
                .build();
    }
}

