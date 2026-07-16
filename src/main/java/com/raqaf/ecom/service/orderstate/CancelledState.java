package com.raqaf.ecom.service.orderstate;

import com.raqaf.ecom.entity.Order;
import com.raqaf.ecom.exception.InvalidOrderStateException;
import org.springframework.stereotype.Component;

@Component
public class CancelledState implements OrderState {

    @Override
    public void next(Order order) {
        throw new InvalidOrderStateException("Cannot progress a cancelled order.");
    }

    @Override
    public void cancel(Order order) {
        throw new InvalidOrderStateException("Order is already cancelled.");
    }
}
