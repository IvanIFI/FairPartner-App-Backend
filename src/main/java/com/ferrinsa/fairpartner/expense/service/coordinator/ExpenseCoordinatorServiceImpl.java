package com.ferrinsa.fairpartner.expense.service.coordinator;

import com.ferrinsa.fairpartner.expense.dto.expense.CreateExpenseRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.expense.ExpenseDetailsResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.expense.ExpenseSummaryResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.expense.UpdateExpenseRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseCoordinatorServiceImpl implements ExpenseCoordinatorService {

    @Override
    public ExpenseDetailsResponseDTO createExpense(Long authUserID, CreateExpenseRequestDTO createExpenseRequestDTO) {
        return null;
    }

    @Override
    public void deleteExpense(Long authUserId, Long expenseId) {

    }

    @Override
    public ExpenseDetailsResponseDTO updateExpense(Long authUserId, Long expenseId, UpdateExpenseRequestDTO updateExpenseRequestDTO) {
        return null;
    }

    @Override
    public ExpenseDetailsResponseDTO getExpenseDetails(Long authUserId, Long expenseId) {
        return null;
    }

    @Override
    public List<ExpenseSummaryResponseDTO> getListExpenses(Long authUserId, Long expenseGroupId) {
        return List.of();
    }

}
