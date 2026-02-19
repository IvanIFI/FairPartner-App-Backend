package com.ferrinsa.fairpartner.exception.expense.expensegroup;

import com.ferrinsa.fairpartner.exception.AppException;

public class ExpenseGroupNotFoundException  extends AppException {

    public ExpenseGroupNotFoundException(String value) {
        super("EXPENSE_GROUP_NOT_FOUND", "No se ha encontrado el grupo de gastos con id: " + value);
    }
}
