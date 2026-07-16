package com.raqaf.ecom.service.orderstate;

import com.raqaf.ecom.entity.Order;

/**
 * CONCEPT: State Design Pattern.
 * Instead of a service method full of:
 *   if (order.getStatus() == PLACED) { ... allow CONFIRMED or CANCELLED ... }
 *   else if (order.getStatus() == CONFIRMED) { ... allow SHIPPED ... }
 *   else if ...
 * each state is its own class that knows ONLY what transitions are legal
 * FROM that state. Adding a new status later means adding a new class,
 * not editing a giant if/else chain (Open/Closed Principle again).
 */
public interface OrderState {

    /** Move the order forward to its next logical status. */
    void next(Order order);

    /** Cancel the order, if cancellation is allowed from this state. */
    void cancel(Order order);
}