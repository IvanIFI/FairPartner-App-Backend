package com.ferrinsa.fairpartner.expense.repository;

import com.ferrinsa.fairpartner.expense.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {

    List<Expense> findByExpenseGroupId(Long groupId);

    List<Expense> findByName(String name);

    List<Expense> findByCategoryId(Long categoryId);

    List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Expense> findByAmountBetween(BigDecimal min, BigDecimal max);

    @Query("""
               SELECT COALESCE(SUM(ex.amount), 0)
               FROM Expense ex
               WHERE ex.expenseGroup.id = :expenseGroupId
            """)
    BigDecimal sumTotalAmountsByGroup(Long expenseGroupId);

}
