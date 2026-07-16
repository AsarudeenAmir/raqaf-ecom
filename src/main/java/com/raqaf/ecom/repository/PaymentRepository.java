package com.raqaf.ecom.repository;


import com.raqaf.ecom.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}

