package com.ferrinsa.fairpartner.expense.repository;

import com.ferrinsa.fairpartner.expense.model.Payment;
import com.ferrinsa.fairpartner.expense.model.compositeid.PaymentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, PaymentId> {

    Optional<Payment> findByExpenseId(Long expenseId);

    @Query("""
               SELECT COALESCE(SUM(pay.amount), 0)
               FROM Payment pay
               WHERE pay.user.id = :userId
               AND pay.expense.expenseGroup.id = :expenseGroupId
            """)
    BigDecimal sumAmountByUserAndGroup(Long userId, Long expenseGroupId);

}
