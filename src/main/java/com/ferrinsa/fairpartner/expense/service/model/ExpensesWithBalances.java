package com.ferrinsa.fairpartner.expense.service.model;

import com.ferrinsa.fairpartner.expense.service.domain.balance.model.UserBalanceResult;

import java.util.List;

public record ExpensesWithBalances(
        List<ExpenseSummary> expenses,
        List<UserBalanceResult> balances) {
}
