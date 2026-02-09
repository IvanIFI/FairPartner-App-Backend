package com.ferrinsa.fairpartner.expense.dto.expensegroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewExpenseGroupRequestDTO(
        @NotBlank @Size(max = 20) String name,
        @Size(max = 200) String description,
        @NotBlank @Size(max = 300) String icon

) {
}
