package com.raqaf.ecom.service.payment;




import com.raqaf.ecom.entity.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * CONCEPT: Factory Design Pattern.
 * Hides the "which class do I instantiate/inject" decision behind one
 * method. The controller/service just says "give me a UPI payment
 * handler" and doesn't need a big switch statement scattered everywhere
 * a payment might be processed.
 */
@Component
@RequiredArgsConstructor
public class PaymentStrategyFactory {

    @Qualifier("creditCardPayment")
    private final PaymentStrategy creditCardPayment;

    @Qualifier("upiPayment")
    private final PaymentStrategy upiPayment;

    @Qualifier("walletPayment")
    private final PaymentStrategy walletPayment;

    public PaymentStrategy getStrategy(PaymentMethod method) {
        return switch (method) {
            case CREDIT_CARD -> creditCardPayment;
            case UPI -> upiPayment;
            case WALLET -> walletPayment;
        };
    }
}
