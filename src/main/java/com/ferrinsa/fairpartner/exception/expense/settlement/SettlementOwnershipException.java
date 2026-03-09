package com.ferrinsa.fairpartner.exception.expense.settlement;

import com.ferrinsa.fairpartner.exception.AppException;

public class SettlementOwnershipException extends AppException {

    public SettlementOwnershipException() {
        super("SETTLEMENT_OWNERSHIP", "Solo el creador de la liquidación puede modificarla o eliminarla.");
    }

}
