package com.ferrinsa.fairpartner.expense.service.coordinator;

import com.ferrinsa.fairpartner.expense.dto.expense.*;
import com.ferrinsa.fairpartner.expense.model.Expense;
import com.ferrinsa.fairpartner.expense.service.coordinator.model.ExpensesWithBalances;

public interface ExpenseCoordinatorService {


    Expense createExpense(Long authUserID,
                          CreateExpenseRequestDTO createExpenseRequestDTO);

    Expense getExpenseDetails(Long authUserId, Long expenseId);

    ExpensesWithBalances getListExpenses(Long authUserId, Long expenseGroupId);

    void deleteExpense(Long authUserId, Long expenseId);

    Expense updateExpense(Long authUserId, Long expenseId, UpdateExpenseRequestDTO updateExpenseRequestDTO);

}
