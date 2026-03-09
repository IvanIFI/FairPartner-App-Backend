package com.ferrinsa.fairpartner.expense.dto.settlement;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record NewSettlementRequestDTO(
        @NotNull Long expenseGroupId,
        @NotNull Long toUserId,
        @Positive @NotNull BigDecimal amount
) {
}
