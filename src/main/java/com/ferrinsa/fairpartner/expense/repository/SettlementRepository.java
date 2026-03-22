package com.ferrinsa.fairpartner.expense.repository;

import com.ferrinsa.fairpartner.expense.model.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {

    List<Settlement> findByExpenseGroupId(Long expenseGroupId);

    @Query("""
               SELECT COALESCE(SUM(s.amount), 0)
               FROM Settlement s
               WHERE s.fromUser.id = :userId
               AND s.expenseGroup.id = :expenseGroupId
            """)
    BigDecimal sumAmountSentByUserAndGroup(Long userId, Long expenseGroupId);

    @Query("""
               SELECT COALESCE(SUM(s.amount), 0)
               FROM Settlement s
               WHERE s.toUser.id = :userId
               AND s.expenseGroup.id = :expenseGroupId
            """)
    BigDecimal sumAmountReceivedByUserAndGroup(Long userId, Long expenseGroupId);

}
