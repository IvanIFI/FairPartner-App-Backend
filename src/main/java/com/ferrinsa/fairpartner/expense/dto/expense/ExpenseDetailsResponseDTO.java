package com.ferrinsa.fairpartner.expense.dto.expense;

import com.ferrinsa.fairpartner.expense.model.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseDetailsResponseDTO(
        Long id,
        String expenseGroupName,
        String categoryName,
        String creatorEmail,
        String name,
        String description,
        String icon,
        LocalDate createdDate,
        BigDecimal amount
) {

    public static ExpenseDetailsResponseDTO of(Expense expense) {
        return new ExpenseDetailsResponseDTO(
                expense.getId(),
                expense.getExpenseGroup().getName(),
                expense.getCategory().getName(),
                expense.getCreatedBy().getEmail(),
                expense.getName(),
                expense.getDescription(),
                expense.getIcon(),
                expense.getCreatedDate(),
                expense.getAmount()
        );
    }

}
