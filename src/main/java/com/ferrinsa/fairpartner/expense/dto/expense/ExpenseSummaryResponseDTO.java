package com.ferrinsa.fairpartner.expense.dto.expense;

import com.ferrinsa.fairpartner.expense.model.Expense;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseSummaryResponseDTO(
        Long id,
        String name,
        String description,
        String icon,
        LocalDateTime createdDate,
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
