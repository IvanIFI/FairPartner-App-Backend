package com.ferrinsa.fairpartner.expense.repository;

import com.ferrinsa.fairpartner.expense.model.Payment;
import com.ferrinsa.fairpartner.expense.model.compositeid.PaymentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, PaymentId> {

    Optional<Payment> findByExpenseId(Long expenseId);

}
