package com.raqaf.ecom.service;


import com.raqaf.ecom.dto.PaymentRequest;
import com.raqaf.ecom.entity.Order;
import com.raqaf.ecom.entity.OrderStatus;
import com.raqaf.ecom.entity.Payment;
import com.raqaf.ecom.exception.OrderNotFoundException;
import com.raqaf.ecom.exception.PaymentFailedException;
import com.raqaf.ecom.repository.OrderRepository;
import com.raqaf.ecom.repository.PaymentRepository;
import com.raqaf.ecom.service.payment.PaymentResult;
import com.raqaf.ecom.service.payment.PaymentStrategy;
import com.raqaf.ecom.service.payment.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentStrategyFactory paymentStrategyFactory;

    @Transactional
    public Payment payForOrder(PaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + request.getOrderId()));

        if (order.getStatus() != OrderStatus.PLACED) {
            throw new PaymentFailedException("Order is not in a payable state (current status: " + order.getStatus() + ")");
        }

        // CONCEPT: Factory picks the right Strategy based on the enum,
        // then we just call .process() without caring which implementation it is.
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(request.getPaymentMethod());
        PaymentResult result = strategy.process(order.getTotalAmount());

        if (!result.isSuccess()) {
            throw new PaymentFailedException("Payment failed: " + result.getMessage());
        }

        Payment payment = Payment.builder()
                .order(order)
                .method(request.getPaymentMethod())
                .amount(order.getTotalAmount())
                .successful(true)
                .transactionId(result.getTransactionId())
                .build();

        // Payment succeeded -> move order from PLACED to CONFIRMED
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        return paymentRepository.save(payment);
    }
}
