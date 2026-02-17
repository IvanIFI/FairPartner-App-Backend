package com.ferrinsa.fairpartner.expense.service;

import com.ferrinsa.fairpartner.expense.dto.expensegroup.ExpenseGroupResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.expensegroup.NewExpenseGroupRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.expensegroup.UpdateExpenseGroupRequestDTO;


import java.util.List;

public interface ExpenseGroupService {

    List<ExpenseGroupResponseDTO> findExpenseGroupsByUser(Long authUserId);

    ExpenseGroupResponseDTO findExpenseGroupById(Long authUserId, Long groupId);

    ExpenseGroupResponseDTO createExpenseGroup(Long authUserId,
                                               NewExpenseGroupRequestDTO newExpenseGroupRequestDTO);

    ExpenseGroupResponseDTO addUserToExpenseGroup(Long authUserId, Long expenseGroupId);

    void leaveCurrentUserFromExpenseGroup(Long authUserId, Long expenseGroupId);

    ExpenseGroupResponseDTO updateExpenseGroup(Long authUserId,
                                               Long expenseGroupId,
                                               UpdateExpenseGroupRequestDTO updateExpenseGroupRequestDTO);

}
