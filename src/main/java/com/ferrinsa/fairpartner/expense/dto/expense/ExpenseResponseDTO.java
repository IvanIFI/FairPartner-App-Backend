package com.ferrinsa.fairpartner.expense.dto.expense;

import com.ferrinsa.fairpartner.expense.model.Expense;

import java.time.LocalDate;

public record ExpenseResponseDTO(
        Long expenseGroupId,
        Long categoryId,
        String name,
        String description,
        LocalDate date,
        String icon,
        Double cant
) {

    public static ExpenseResponseDTO of(Expense expense) {
        return new ExpenseResponseDTO(
                expense.getExpenseGroup().getId(),
                expense.getCategory().getId(),
                expense.getName(),
                expense.getDescription(),
                expense.getDate(),
                expense.getIcon(),
                expense.getCant());
    }
}
