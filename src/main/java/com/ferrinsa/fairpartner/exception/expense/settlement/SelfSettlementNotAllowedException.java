package com.ferrinsa.fairpartner.exception.expense.settlement;

import com.ferrinsa.fairpartner.exception.AppException;

public class SelfSettlementNotAllowedException extends AppException {

    public SelfSettlementNotAllowedException() {
        super("SELF_SETTLEMENT_NOT_ALLOWED", "No se permite hacer una liquidación a uno mismo.");
    }

}
