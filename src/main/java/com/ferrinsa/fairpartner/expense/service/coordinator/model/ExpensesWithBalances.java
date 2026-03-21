package com.ferrinsa.fairpartner.expense.service.coordinator.model;

import com.ferrinsa.fairpartner.expense.model.Expense;
import com.ferrinsa.fairpartner.expense.service.domain.balance.model.UserBalanceResult;

import java.util.List;

public record ExpensesWithBalances(
        List<Expense> expenses,
        List<UserBalanceResult> balances) {
}
