package com.ferrinsa.fairpartner.exception.balance;

import com.ferrinsa.fairpartner.exception.AppException;

public class ExpenseShareIntegrityException extends AppException {

    public ExpenseShareIntegrityException() {
        super("AMOUNT_INTEGRITY_ERROR", "No es posible calcular el balance por inconsistencia en las cantidades.");
    }
}
