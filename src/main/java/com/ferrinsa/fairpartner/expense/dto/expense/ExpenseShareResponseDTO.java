package com.ferrinsa.fairpartner.expense.dto.expense;

import com.ferrinsa.fairpartner.expense.model.ExpenseShare;

import java.math.BigDecimal;

public record ExpenseShareResponseDTO(
        Long userId,
        String name,
        BigDecimal amount
) {

    public static ExpenseShareResponseDTO of(ExpenseShare expenseShare) {
        return new ExpenseShareResponseDTO(
                expenseShare.getUser().getId(),
                expenseShare.getUser().getName(),
                expenseShare.getAmount()
        );
    }

}
