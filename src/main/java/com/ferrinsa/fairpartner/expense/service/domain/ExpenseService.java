package com.ferrinsa.fairpartner.expense.service.domain;

import com.ferrinsa.fairpartner.expense.model.Expense;

import java.util.List;

public interface ExpenseService {

    Expense createExpense(Expense newExpense);

    Expense udpateExpense(Long expenseId, Expense expenseNewValues);

    void deleteExpense(Long expenseToDeleteId);

    Expense findById(Long expenseId);

    List<Expense> findByGroupId(Long expenseGroupId);

}
