package com.ferrinsa.fairpartner.expense.service.domain;

import com.ferrinsa.fairpartner.expense.model.ExpenseShare;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseShareService {

    ExpenseShare createExpenseShare(ExpenseShare newExpenseShare);

    ExpenseShare updateExpenseShare(Long expenseId, Long userId, BigDecimal newAmount);

    void deleteByExpenseId(Long expenseId);

    List<ExpenseShare> findByExpenseId(Long expenseId);

}
