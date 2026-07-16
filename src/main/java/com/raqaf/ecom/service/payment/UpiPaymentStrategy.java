package com.raqaf.ecom.service.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component("upiPayment")
@Slf4j
public class UpiPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult process(BigDecimal amount) {
        log.info("Processing UPI payment of {}", amount);
        return PaymentResult.builder()
                .success(true)
                .transactionId("UPI-" + UUID.randomUUID())
                .message("UPI payment successful")
                .build();
    }
}