package com.ferrinsa.fairpartner.expense.dto.expense;

import com.ferrinsa.fairpartner.expense.service.coordinator.model.ExpenseShareRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record UpdateExpenseRequestDTO(
        @NotNull Long expenseId,
        @NotNull Long categoryId,
        @NotBlank @Size(max = 20) String name,
        @Size(max = 200) String description,
        @NotNull Long payerUserId,
        @NotNull @Size(min = 1) List<ExpenseShareRequest> expenseShares,
        @NotNull @Positive BigDecimal amount
) {
}
