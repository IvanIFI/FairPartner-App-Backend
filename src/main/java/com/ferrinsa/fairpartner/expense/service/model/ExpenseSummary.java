package com.ferrinsa.fairpartner.expense.service.model;

import java.math.BigDecimal;
import java.time.Instant;

public record ExpenseSummary(
        Long id,
        String name,
        String description,
        String icon,
        Instant createdDate,
        BigDecimal amount,
        PayerResponse payer
) {

}
