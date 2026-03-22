package com.ferrinsa.fairpartner.expense.service.model;

import com.ferrinsa.fairpartner.expense.model.Expense;
import com.ferrinsa.fairpartner.expense.model.ExpenseShare;
import com.ferrinsa.fairpartner.user.model.UserEntity;

import java.util.List;

public record ExpenseWithSharesAndPayer(
        Expense expense,
        List<ExpenseShare> shares,
        UserEntity payer) {
}
