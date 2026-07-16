package com.raqaf.ecom.dto;


import com.raqaf.ecom.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull(message = "orderId is required")
    private Long orderId;

    @NotNull(message = "paymentMethod is required")
    private PaymentMethod paymentMethod;
}
