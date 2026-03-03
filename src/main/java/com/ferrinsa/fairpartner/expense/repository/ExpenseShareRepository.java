package com.ferrinsa.fairpartner.expense.repository;

import com.ferrinsa.fairpartner.expense.model.ExpenseShare;
import com.ferrinsa.fairpartner.expense.model.compositeid.ExpenseShareId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ExpenseShareRepository extends JpaRepository<ExpenseShare, ExpenseShareId> {

    Optional<ExpenseShare> findByExpenseIdAndUserId(Long expenseId, Long userId);

    List<ExpenseShare> findByExpenseId(Long expenseId);

    void deleteByExpenseId(Long expenseId);

    @Query("""
               SELECT COALESCE(SUM(es.amount), 0)
               FROM ExpenseShare es
               WHERE es.expense.expenseGroup.id = :expenseGroupId
            """)
    BigDecimal sumAmountExpenseShareByGroup(Long expenseGroupId);


    @Query("""
               SELECT COALESCE(SUM(es.amount), 0)
               FROM ExpenseShare es
               WHERE es.user.id = :userId
               AND es.expense.expenseGroup.id = :expenseGroupId
            """)
    BigDecimal sumAmountByUserAndGroup(Long userId, Long expenseGroupId);

}
