package com.ferrinsa.fairpartner.expense.service.impl;

import com.ferrinsa.fairpartner.expense.repository.PaymentRepository;
import com.ferrinsa.fairpartner.expense.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
