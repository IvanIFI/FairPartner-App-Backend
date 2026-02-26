package com.ferrinsa.fairpartner.expense.dto.expense;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateExpenseRequestDTO(
        @NotNull Long expenseGroupId,
        @NotNull Long categoryId,
        @NotBlank @Size(max = 20) String name,
        @Size(max = 200) String description,
        @NotNull @Positive BigDecimal amount
) {
}
