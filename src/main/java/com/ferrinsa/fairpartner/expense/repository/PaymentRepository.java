package com.ferrinsa.fairpartner.expense.repository;

import com.ferrinsa.fairpartner.expense.model.Payment;
import com.ferrinsa.fairpartner.expense.model.compositeid.PaymentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, PaymentId> {
}
