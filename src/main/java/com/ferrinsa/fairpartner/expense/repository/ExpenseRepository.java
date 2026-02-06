package com.ferrinsa.fairpartner.expense.repository;

import com.ferrinsa.fairpartner.expense.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {

    List<Expense> findByExpenseGroupId(Long groupId);

    List<Expense> findByName(String name);

    List<Expense> findByCategoryId(Long categoryId);

    List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Expense> findByCantBetween(Double min,Double max);

}
