package com.ferrinsa.fairpartner.exception.expense.settlement;

import com.ferrinsa.fairpartner.exception.AppException;

public class NotDebtException extends AppException {

    public NotDebtException() {
        super("NOT_DEBT", "No hay deuda que liquidar.");
    }

}
