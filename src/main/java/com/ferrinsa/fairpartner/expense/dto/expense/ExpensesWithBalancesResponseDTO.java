package com.ferrinsa.fairpartner.expense.dto.expense;

import com.ferrinsa.fairpartner.expense.service.model.ExpensesWithBalances;

import java.util.List;

public record ExpensesWithBalancesResponseDTO(
        List<ExpenseSummaryResponseDTO> expenses,
        List<UserBalanceResultDTO> balances
) {
    public static ExpensesWithBalancesResponseDTO of(ExpensesWithBalances expensesWithBalances) {
        return new ExpensesWithBalancesResponseDTO(
                expensesWithBalances.expenses().stream()
                        .map(ExpenseSummaryResponseDTO::of)
                        .toList(),
                expensesWithBalances.balances().stream()
                        .map(UserBalanceResultDTO::of)
                        .toList()
        );
    }
}
