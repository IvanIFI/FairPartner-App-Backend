package com.ferrinsa.fairpartner.expense.repository;

import com.ferrinsa.fairpartner.expense.model.ExpenseGroup;
import com.ferrinsa.fairpartner.expense.model.Participate;
import com.ferrinsa.fairpartner.expense.model.compositeid.ParticipateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParticipateRepository extends JpaRepository<Participate, ParticipateId> {

    void deleteByUserIdAndExpenseGroupId(Long userId, Long expenseGroupId);

    boolean existsByUserIdAndExpenseGroupId(Long userId, Long expenseGroupId);

    @Query("""
        SELECT DISTINCT g
        FROM ExpenseGroup g
        JOIN Participate pUser ON pUser.expenseGroup = g
        JOIN FETCH Participate pMembers ON pMembers.expenseGroup = g
        JOIN FETCH pMembers.user
        WHERE pUser.user.id = :userId
    """)
    List<ExpenseGroup> findExpenseGroupsWithUsersByUserId(Long userId);

}
