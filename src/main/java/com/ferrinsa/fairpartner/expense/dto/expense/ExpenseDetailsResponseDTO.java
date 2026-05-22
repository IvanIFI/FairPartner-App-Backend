package com.ferrinsa.fairpartner.expense.dto.expense;

import com.ferrinsa.fairpartner.expense.model.Expense;
import com.ferrinsa.fairpartner.expense.service.model.ExpenseWithSharesAndPayer;
import com.ferrinsa.fairpartner.expense.service.model.PayerResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ExpenseDetailsResponseDTO(
        Long id,
        String expenseGroupName,
        String categoryName,
        String creatorEmail,
        String name,
        String description,
        String icon,
        Instant createdDate,
        BigDecimal amount,
        PayerResponse payer,
        List<ExpenseShareResponseDTO> expenseShares
) {

    public static ExpenseDetailsResponseDTO of(ExpenseWithSharesAndPayer expenseWithSharesAndPayer) {
        Expense expense = expenseWithSharesAndPayer.expense();
        return new ExpenseDetailsResponseDTO(
                expense.getId(),
                expense.getExpenseGroup().getName(),
                expense.getCategory().getName(),
                expense.getCreatedBy().getEmail(),
                expense.getName(),
                expense.getDescription(),
                expense.getIcon(),
                expense.getCreatedDate(),
                expense.getAmount(),
                new PayerResponse(
                        expenseWithSharesAndPayer.payer().getId(),
                        expenseWithSharesAndPayer.payer().getName()
                ),
                expenseWithSharesAndPayer.shares().stream()
                        .map(ExpenseShareResponseDTO::of)
                        .toList()
        );
    }

}
