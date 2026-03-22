package com.ferrinsa.fairpartner.expense.dto.expense;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ExpenseShareRequestDTO(
        @NotNull Long userId,
        @NotNull @Positive BigDecimal amount
) {
}
