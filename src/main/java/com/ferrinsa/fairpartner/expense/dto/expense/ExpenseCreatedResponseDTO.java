package com.ferrinsa.fairpartner.expense.dto.expense;

import com.ferrinsa.fairpartner.expense.model.Expense;

import java.time.LocalDate;

public record ExpenseCreatedResponseDTO(
        Long id,
        Long expenseGroupId,
        Long categoryId,
        String name,
        String description,
        LocalDate date,
        String icon,
        Double cant,
        String resourceUrl
) {

    public static final String BASE_RESOURCE_URL = "https://ferrinsa.api/expense/";

    public static ExpenseCreatedResponseDTO of(Expense expense) {
        return new ExpenseCreatedResponseDTO(
                expense.getId(),
                expense.getExpenseGroup().getId(),
                expense.getCategory().getId(),
                expense.getName(),
                expense.getDescription(),
                expense.getDate(),
                expense.getIcon(),
                expense.getCant(),
                BASE_RESOURCE_URL + expense.getId());
    }
}
