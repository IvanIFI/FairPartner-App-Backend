package com.ferrinsa.fairpartner.exception.expense.settlement;

import com.ferrinsa.fairpartner.exception.AppException;

public class DebtLimitExceededException extends AppException {

    public DebtLimitExceededException() {
        super("DEBT_LIMIT_SETTLEMENT_EXCEEDED", "No se puede liquidar un importe mayor a la deuda.");
    }

}
