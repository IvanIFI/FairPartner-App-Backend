package com.ferrinsa.fairpartner.expense.service.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ExpenseShareRequest(
        @NotNull Long userId,
        @NotNull @Positive BigDecimal amount
) {}
