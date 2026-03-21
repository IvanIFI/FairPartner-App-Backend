package com.ferrinsa.fairpartner.expense.dto.expense;

import com.ferrinsa.fairpartner.expense.service.domain.balance.model.UserBalanceResult;

import java.util.List;

public record ExpenseListWithBalanceResponseDTO(
        List<ExpenseSummaryResponseDTO> expenses,
        List<UserBalanceResult> balances
) {
}
