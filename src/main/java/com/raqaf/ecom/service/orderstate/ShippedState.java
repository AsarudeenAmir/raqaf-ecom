package com.raqaf.ecom.service.orderstate;


import com.raqaf.ecom.entity.Order;
import com.raqaf.ecom.entity.OrderStatus;
import com.raqaf.ecom.exception.InvalidOrderStateException;
import org.springframework.stereotype.Component;

@Component
public class ShippedState implements OrderState {

    @Override
    public void next(Order order) {
        order.setStatus(OrderStatus.DELIVERED);
    }

    @Override
    public void cancel(Order order) {
        // CONCEPT: this is the whole point of the pattern - once shipped,
        // cancellation is a business rule violation, and this state is the
        // ONLY place that rule needs to be encoded.
        throw new InvalidOrderStateException("Cannot cancel an order that has already been shipped.");
    }
}