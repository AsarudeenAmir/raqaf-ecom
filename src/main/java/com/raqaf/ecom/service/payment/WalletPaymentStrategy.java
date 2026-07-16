package com.raqaf.ecom.service.payment;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component("walletPayment")
@Slf4j
public class WalletPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult process(BigDecimal amount) {
        log.info("Processing wallet payment of {}", amount);
        return PaymentResult.builder()
                .success(true)
                .transactionId("WAL-" + UUID.randomUUID())
                .message("Wallet payment successful")
                .build();
    }
}
