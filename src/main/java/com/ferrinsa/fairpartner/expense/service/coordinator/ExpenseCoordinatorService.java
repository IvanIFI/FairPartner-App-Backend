package com.ferrinsa.fairpartner.expense.service.coordinator;

import com.ferrinsa.fairpartner.expense.dto.expense.*;

import java.util.List;

public interface ExpenseCoordinatorService {


    ExpenseDetailsResponseDTO createExpense(Long authUserID,
                                            CreateExpenseRequestDTO createExpenseRequestDTO);

    void deleteExpense(Long authUserId, Long expenseId);

    ExpenseDetailsResponseDTO updateExpense(Long authUserId,
                                            Long expenseId,
                                            UpdateExpenseRequestDTO updateExpenseRequestDTO);

    ExpenseDetailsResponseDTO getExpenseDetails(Long authUserId, Long expenseId);

    ExpenseListWithBalanceResponseDTO getListExpenses(Long authUserId, Long expenseGroupId);

}
