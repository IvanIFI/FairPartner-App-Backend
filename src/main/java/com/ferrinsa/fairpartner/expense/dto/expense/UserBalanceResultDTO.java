package com.ferrinsa.fairpartner.expense.dto.expense;

import com.ferrinsa.fairpartner.user.dto.UserResponseDTO;

import java.math.BigDecimal;

public record UserBalanceResultDTO(
        UserResponseDTO user,
        BigDecimal balance) {
}
