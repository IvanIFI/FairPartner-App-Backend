package com.ferrinsa.fairpartner.exception.expense.expense;

import com.ferrinsa.fairpartner.exception.AppException;

public class ExpenseNotFoundException extends AppException {
    public ExpenseNotFoundException(String value) {
        super("EXPENSE_NOT_FOUND", "No se ha encontrado el gasto con id: " + value);
    }
}
