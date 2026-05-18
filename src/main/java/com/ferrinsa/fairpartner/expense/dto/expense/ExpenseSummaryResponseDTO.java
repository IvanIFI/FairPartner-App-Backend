package com.ferrinsa.fairpartner.expense.dto.expense;

import com.ferrinsa.fairpartner.expense.model.Expense;

import java.math.BigDecimal;
import java.time.Instant;

public record ExpenseSummaryResponseDTO(
        Long id,
        String name,
        String description,
        String icon,
        Instant createdDate,
        BigDecimal amount
) {
    public static ExpenseSummaryResponseDTO of(Expense expense) {
        return new ExpenseSummaryResponseDTO(
                expense.getId(),
                expense.getName(),
                expense.getDescription(),
                expense.getIcon(),
                expense.getCreatedDate(),
                expense.getAmount()
        );
    }
}
