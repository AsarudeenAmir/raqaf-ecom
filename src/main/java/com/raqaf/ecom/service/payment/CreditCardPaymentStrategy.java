package com.raqaf.ecom.service.payment;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * SIMULATED payment processor - no real gateway is called here.
 * In a real project, this is where you'd call Stripe/Razorpay/PayPal's
 * SDK. Swapping the real API in later means changing ONLY this class -
 * nothing else in the codebase needs to know.
 */
@Component("creditCardPayment")
@Slf4j
public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult process(BigDecimal amount) {
        log.info("Processing credit card payment of {}", amount);
        // Simulate success
        return PaymentResult.builder()
                .success(true)
                .transactionId("CC-" + UUID.randomUUID())
                .message("Credit card payment successful")
                .build();
    }
}