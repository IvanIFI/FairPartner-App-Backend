package com.ferrinsa.fairpartner.exception.expense.settlement;

import com.ferrinsa.fairpartner.exception.AppException;

public class SettlementNotFoundException extends AppException {

    public SettlementNotFoundException(String value) {
        super("SETTLEMENT_NOT_FOUND", "No se ha encontrado la liquidación con id: " + value);
    }

}
