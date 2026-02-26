package com.ferrinsa.fairpartner.expense.service.domain.impl;

import com.ferrinsa.fairpartner.exception.expense.expense.ExpenseNotFoundException;
import com.ferrinsa.fairpartner.exception.expense.payment.PaymentNotFoundException;
import com.ferrinsa.fairpartner.expense.model.Expense;
import com.ferrinsa.fairpartner.expense.model.Payment;
import com.ferrinsa.fairpartner.expense.repository.ExpenseRepository;
import com.ferrinsa.fairpartner.expense.repository.PaymentRepository;
import com.ferrinsa.fairpartner.expense.service.domain.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public Payment createPayment(Payment payment) {
        paymentRepository.save(payment);
        return payment;
    }

    /*
     * The payer (user) is not updated here because it is part of the composite primary key.
     * Changing the payer would require modifying the primary key, which is not supported
     * by JPA for managed entities.
     *
     * If the payer needs to change, this must be handled at the Coordinator level
     * by deleting the existing Payment and creating a new one.
     */
    @Override
    @Transactional
    public Payment updatePayment(Long expenseId, Payment newPaymentValues) {
        Payment paymentToUpdate = paymentRepository.findByExpenseId(expenseId)
                .orElseThrow(() -> new PaymentNotFoundException(String.valueOf(expenseId)));

        paymentToUpdate.setAmount(newPaymentValues.getAmount());
        return paymentToUpdate;
    }

    @Override
    @Transactional
    public void deletePayment(Long expenseId) {
        Payment paymentToDelete = paymentRepository.findByExpenseId(expenseId)
                .orElseThrow(() -> new PaymentNotFoundException(String.valueOf(expenseId)));

        paymentRepository.delete(paymentToDelete);
    }

    @Override
    public Payment findByExpenseId(Long expenseId) {
        return paymentRepository.findByExpenseId(expenseId)
                .orElseThrow(() -> new PaymentNotFoundException(String.valueOf(expenseId)));
    }

}
