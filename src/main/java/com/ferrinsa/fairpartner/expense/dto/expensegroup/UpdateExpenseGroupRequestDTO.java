package com.ferrinsa.fairpartner.expense.dto.expensegroup;

// Validation is handled in the service to allow partial updates and reuse of the DTO.
public record UpdateExpenseGroupRequestDTO(
        String name,
        String description,
        String icon
) {
}
