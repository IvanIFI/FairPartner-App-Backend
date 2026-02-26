package com.ferrinsa.fairpartner.exception.expenseshare;

import com.ferrinsa.fairpartner.exception.AppException;

public class ExpenseShareNotFoundException extends AppException {

    public ExpenseShareNotFoundException() {
        super("EXPENSE_NOT_FOUND", "No se ha encontrado el gasto compartido.");
    }

}
