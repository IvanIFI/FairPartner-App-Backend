package com.ferrinsa.fairpartner.expense.repository;

import com.ferrinsa.fairpartner.expense.model.ExpenseShare;
import com.ferrinsa.fairpartner.expense.model.compositeid.ExpenseShareId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseShareRepository extends JpaRepository<ExpenseShare, ExpenseShareId> {

    Optional<ExpenseShare> findByExpenseIdAndUserId(Long expenseId, Long userId);

    List<ExpenseShare> findByExpenseId(Long expenseId);

    void deleteByExpenseId(Long expenseId);
}
