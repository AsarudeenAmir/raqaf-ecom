package com.raqaf.ecom.service.payment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResult {
    private boolean success;
    private String transactionId;
    private String message;
}
