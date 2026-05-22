package com.ferrinsa.fairpartner.expense.dto.expense;

import com.ferrinsa.fairpartner.expense.service.model.ExpenseSummary;
import com.ferrinsa.fairpartner.expense.service.model.PayerResponse;

import java.math.BigDecimal;
import java.time.Instant;

public record ExpenseSummaryResponseDTO(
        Long id,
        String name,
        String description,
        String icon,
        Instant createdDate,
        BigDecimal amount,
        PayerResponse payer
) {
    public static ExpenseSummaryResponseDTO of(ExpenseSummary expense) {
        return new ExpenseSummaryResponseDTO(
                expense.id(),
                expense.name(),
                expense.description(),
                expense.icon(),
                expense.createdDate(),
                expense.amount(),
                expense.payer()
        );
    }
}
