package com.raqaf.ecom.service.orderstate;


import com.raqaf.ecom.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

/**
 * CONCEPT: Context class for the State pattern. Given the order's CURRENT
 * status (an enum, easy to persist in the DB), this looks up which
 * OrderState object should handle the next transition.
 */
@Component
@RequiredArgsConstructor
public class OrderStateContext {

    private final PlacedState placedState;
    private final ConfirmedState confirmedState;
    private final ShippedState shippedState;
    private final DeliveredState deliveredState;
    private final CancelledState cancelledState;

    private Map<OrderStatus, OrderState> stateMap;

    private Map<OrderStatus, OrderState> getStateMap() {
        if (stateMap == null) {
            stateMap = new EnumMap<>(OrderStatus.class);
            stateMap.put(OrderStatus.PLACED, placedState);
            stateMap.put(OrderStatus.CONFIRMED, confirmedState);
            stateMap.put(OrderStatus.SHIPPED, shippedState);
            stateMap.put(OrderStatus.DELIVERED, deliveredState);
            stateMap.put(OrderStatus.CANCELLED, cancelledState);
        }
        return stateMap;
    }

    public OrderState getState(OrderStatus status) {
        return getStateMap().get(status);
    }
}

