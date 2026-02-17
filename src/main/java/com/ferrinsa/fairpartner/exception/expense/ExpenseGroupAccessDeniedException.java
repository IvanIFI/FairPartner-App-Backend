package com.ferrinsa.fairpartner.exception.expense;

import com.ferrinsa.fairpartner.exception.AppException;

public class ExpenseGroupAccessDeniedException extends AppException {

    public ExpenseGroupAccessDeniedException(String userValue, String expenseGroupValue) {
        super("EXPENSE_GROUP_ACCESS_DENIED",
                String.format("User %s cannot access expense group %s", userValue, expenseGroupValue));
    }
}

