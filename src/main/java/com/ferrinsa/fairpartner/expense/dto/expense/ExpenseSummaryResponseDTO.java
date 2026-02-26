package com.ferrinsa.fairpartner.expense.dto.expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseSummaryResponseDTO(
        Long id,
        String name,
        String description,
        String icon,
        LocalDate createdDate,
        BigDecimal amount
) {
}
