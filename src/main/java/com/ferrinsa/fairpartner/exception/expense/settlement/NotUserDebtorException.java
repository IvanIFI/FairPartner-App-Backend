package com.ferrinsa.fairpartner.exception.expense.settlement;

import com.ferrinsa.fairpartner.exception.AppException;

public class NotUserDebtorException extends AppException {

    public NotUserDebtorException() {
        super("USER_IS_NOT_DEBTOR", "El usuario no tiene un balance negativo por lo que no puede liquidar.");
    }

}
