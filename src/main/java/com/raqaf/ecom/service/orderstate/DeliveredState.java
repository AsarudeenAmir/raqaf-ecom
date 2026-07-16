package com.raqaf.ecom.service.orderstate;


import com.raqaf.ecom.entity.Order;
import com.raqaf.ecom.exception.InvalidOrderStateException;
import org.springframework.stereotype.Component;

@Component
public class DeliveredState implements OrderState {

    @Override
    public void next(Order order) {
        throw new InvalidOrderStateException("Order is already delivered, no further transitions possible.");
    }

    @Override
    public void cancel(Order order) {
        throw new InvalidOrderStateException("Cannot cancel a delivered order.");
    }
}