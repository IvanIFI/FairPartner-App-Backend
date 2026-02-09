package com.ferrinsa.fairpartner.expense.service;

import com.ferrinsa.fairpartner.expense.dto.expensegroup.ExpenseGroupResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.expensegroup.NewExpenseGroupRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.expensegroup.UpdateExpenseGroupRequestDTO;
import com.ferrinsa.fairpartner.user.model.UserEntity;


import java.util.List;

public interface ExpenseGroupService {

    List<ExpenseGroupResponseDTO> findExpenseGroupsByUser(UserEntity authUser);

    ExpenseGroupResponseDTO createExpenseGroup(UserEntity authUser,
                                               NewExpenseGroupRequestDTO newExpenseGroupRequestDTO);

    ExpenseGroupResponseDTO addUserToExpenseGroup (UserEntity authUser, Long expenseGroupId);

    void leaveCurrentUserFromExpenseGroup(UserEntity authUser, Long expenseGroupId);

    ExpenseGroupResponseDTO updateExpenseGroup(Long expenseGroupId,
                                               UpdateExpenseGroupRequestDTO updateExpenseGroupRequestDTO);

}
