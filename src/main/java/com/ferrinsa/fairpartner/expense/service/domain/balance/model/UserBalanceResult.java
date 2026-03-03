package com.ferrinsa.fairpartner.expense.service.domain.balance.model;

import com.ferrinsa.fairpartner.user.model.UserEntity;

import java.math.BigDecimal;

public record UserBalanceResult(UserEntity user, BigDecimal balance) {
}
