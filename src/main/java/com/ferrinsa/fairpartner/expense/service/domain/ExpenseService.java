package com.ferrinsa.fairpartner.expense.service.domain;

import com.ferrinsa.fairpartner.expense.model.Expense;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    Expense createExpense(Expense newExpense);

    Expense udpateExpense(Long expenseId, Expense expenseNewValues);

    void deleteExpense(Long expenseToDeleteId);

    Expense findById(Long expenseId);

    List<Expense> findByGroupId(Long expenseGroupId);

    List<Expense> findByName(String name);

    List<Expense> findByCategoryId(Long categoryId);

    List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Expense> findByAmountBetween(BigDecimal min, BigDecimal max);

}
