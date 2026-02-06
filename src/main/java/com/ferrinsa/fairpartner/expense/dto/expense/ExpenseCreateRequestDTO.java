package com.ferrinsa.fairpartner.expense.dto.expense;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ExpenseCreateRequestDTO(
        @NotBlank String name,
        @NotNull Long expenseGroupId,
        @NotNull Long categoryId,
        String description,
        String icon,
        @NotNull @Positive Double cant
) { }