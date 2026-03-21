package com.ferrinsa.fairpartner.exception.expense.expense;

import com.ferrinsa.fairpartner.exception.AppException;

public class UserNotExpenseOwnerException extends AppException {

    public UserNotExpenseOwnerException(String userValue, String expenseValue) {
        super("USER_NOT_EXPENSE_OWNER",
                String.format("El usuario %s no puede eliminar el gasto %s si no es el creador."
                        , userValue, expenseValue));
    }

}
