package com.ferrinsa.fairpartner.expense.dto.expense;

import java.math.BigDecimal;

// Validation is handled in the service to allow partial updates and reuse of the DTO.
public record UpdateExpenseRequestDTO(
        /*String name,
        String description,
        String categoryName,
        BigDecimal amount*/
) {
}
