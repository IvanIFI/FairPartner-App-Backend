package com.ferrinsa.fairpartner.expense.dto.expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseDetailsResponseDTO(
        Long id,
        String expenseGroupName,
        String categoryName,
        String creatorEmail,
        String name,
        String description,
        String icon,
        LocalDate createdDate,
        BigDecimal amount
) {
}
