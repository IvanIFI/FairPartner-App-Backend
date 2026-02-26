package com.ferrinsa.fairpartner.exception.expense.payment;

import com.ferrinsa.fairpartner.exception.AppException;

public class PaymentNotFoundException extends AppException {

        public PaymentNotFoundException(String value) {
            super("PAYMENT_NOT_FOUND", "No se ha encontrado el pago con id: " + value);
        }

}
