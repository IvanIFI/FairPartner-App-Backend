package com.ferrinsa.fairpartner.expense.repository;

import com.ferrinsa.fairpartner.expense.model.ExpenseGroup;
import com.ferrinsa.fairpartner.expense.model.Participate;
import com.ferrinsa.fairpartner.expense.model.compositeid.ParticipateId;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParticipateRepository extends JpaRepository<Participate, ParticipateId> {

    void deleteByUserIdAndExpenseGroupId(Long userId, Long expenseGroupId);

    boolean existsByUserIdAndExpenseGroupId(Long userId, Long expenseGroupId);

    long countByExpenseGroup_Id(Long expenseGroupId);

    @Query("""
                SELECT p.user
                FROM Participate p
                WHERE p.expenseGroup.id = :expenseGroupId
            """)
    List<UserEntity> findUsersByExpenseGroupId(Long expenseGroupId);

    @Query("""
                SELECT DISTINCT g
                FROM ExpenseGroup g
                JOIN g.participates pUser
                JOIN FETCH g.participates pMembers
                JOIN FETCH pMembers.user
                WHERE pUser.user.id = :userId
            """)
    List<ExpenseGroup> findExpenseGroupsWithUsersByUserId(Long userId);

}
