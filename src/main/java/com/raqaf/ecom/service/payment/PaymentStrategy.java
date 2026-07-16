package com.raqaf.ecom.service.payment;

import java.math.BigDecimal;

/**
 * CONCEPT: Strategy Pattern for Payment.
 * Each payment method (Credit Card, UPI, Wallet) has its own way of
 * "processing" a charge - different validation rules, different mock
 * external API calls. The caller (PaymentService) doesn't need to know
 * which one it's using; it just calls process().
 */
public interface PaymentStrategy {
    PaymentResult process(BigDecimal amount);
}