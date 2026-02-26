package com.ferrinsa.fairpartner.expense.service.domain;

import com.ferrinsa.fairpartner.expense.model.Payment;

public interface PaymentService {

    Payment createPayment(Payment payment);

    Payment updatePayment(Long expenseId, Payment newPaymentValues);

    void deletePayment(Long expenseId);

    Payment findByExpenseId(Long expenseId);

}
