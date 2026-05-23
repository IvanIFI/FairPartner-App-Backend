package com.ferrinsa.fairpartner.expense.service.domain;

import com.ferrinsa.fairpartner.expense.dto.expensegroup.NewExpenseGroupRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.expensegroup.UpdateExpenseGroupRequestDTO;
import com.ferrinsa.fairpartner.expense.model.ExpenseGroup;

import java.util.List;

public interface ExpenseGroupService {

    List<ExpenseGroup> findExpenseGroupsByUser(Long authUserId);

    ExpenseGroup findExpenseGroupById(Long authUserId, Long groupId);

    ExpenseGroup createExpenseGroup(Long authUserId,
                                               NewExpenseGroupRequestDTO newExpenseGroupRequestDTO);

    void leaveCurrentUserFromExpenseGroup(Long authUserId, Long expenseGroupId);

    ExpenseGroup updateExpenseGroup(Long authUserId,
                                               Long expenseGroupId,
                                               UpdateExpenseGroupRequestDTO updateExpenseGroupRequestDTO);

}
