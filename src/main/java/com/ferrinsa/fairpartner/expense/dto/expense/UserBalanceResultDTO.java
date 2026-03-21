package com.ferrinsa.fairpartner.expense.dto.expense;

import com.ferrinsa.fairpartner.expense.service.domain.balance.model.UserBalanceResult;
import com.ferrinsa.fairpartner.user.dto.UserResponseDTO;

import java.math.BigDecimal;

public record UserBalanceResultDTO(
        UserResponseDTO user,
        BigDecimal balance) {
    public static UserBalanceResultDTO of(UserBalanceResult userBalanceResult) {
        return new UserBalanceResultDTO(
                UserResponseDTO.of(userBalanceResult.user()),
                userBalanceResult.balance()
        );
    }
}
