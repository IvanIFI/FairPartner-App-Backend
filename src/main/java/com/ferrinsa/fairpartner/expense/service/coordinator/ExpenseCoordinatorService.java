package com.ferrinsa.fairpartner.expense.service.coordinator;

import com.ferrinsa.fairpartner.expense.dto.expense.CreateExpenseRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.expense.UpdateExpenseRequestDTO;
import com.ferrinsa.fairpartner.expense.service.model.ExpenseWithSharesAndPayer;
import com.ferrinsa.fairpartner.expense.service.model.ExpensesWithBalances;

public interface ExpenseCoordinatorService {


    ExpenseWithSharesAndPayer createExpense(Long authUserID,
                          CreateExpenseRequestDTO createExpenseRequestDTO);

    ExpenseWithSharesAndPayer getExpenseDetails(Long authUserId, Long expenseId);

    ExpensesWithBalances getListExpenses(Long authUserId, Long expenseGroupId);

    void deleteExpense(Long authUserId, Long expenseId);

    ExpenseWithSharesAndPayer updateExpense(Long authUserId, Long expenseId, UpdateExpenseRequestDTO updateExpenseRequestDTO);

}
