package com.raqaf.ecom.service.orderstate;


import com.raqaf.ecom.entity.Order;
import com.raqaf.ecom.entity.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class ConfirmedState implements OrderState {

    @Override
    public void next(Order order) {
        order.setStatus(OrderStatus.SHIPPED);
    }

    @Override
    public void cancel(Order order) {
        order.setStatus(OrderStatus.CANCELLED);
    }
}
