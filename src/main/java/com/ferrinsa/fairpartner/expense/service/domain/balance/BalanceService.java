package com.ferrinsa.fairpartner.expense.service.domain.balance;

import com.ferrinsa.fairpartner.expense.service.domain.balance.model.UserBalanceResult;

import java.util.List;

public interface BalanceService {

    List<UserBalanceResult> obtainUsersBalance(Long expenseGroupId);

}
