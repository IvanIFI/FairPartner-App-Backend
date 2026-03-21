package com.ferrinsa.fairpartner.exception.expenseshare;

import com.ferrinsa.fairpartner.exception.AppException;

public class PayerNotInSharesException extends AppException {

    public PayerNotInSharesException() {
        super("PAYER_NOT_IN_SHARES", "El pagador debe estar en la repartición de gastos");
    }

}
