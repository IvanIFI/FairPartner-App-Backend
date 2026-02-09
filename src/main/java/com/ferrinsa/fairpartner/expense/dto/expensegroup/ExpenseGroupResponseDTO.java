package com.ferrinsa.fairpartner.expense.dto.expensegroup;

import com.ferrinsa.fairpartner.expense.model.ExpenseGroup;

import java.util.List;

public record ExpenseGroupResponseDTO(
        Long id,
        String name,
        String description,
        String icon,
        List<UserMemberExpenseGroupDTO> users
) {

    public static ExpenseGroupResponseDTO of(ExpenseGroup expenseGroup) {
        return new ExpenseGroupResponseDTO(
                expenseGroup.getId(),
                expenseGroup.getName(),
                expenseGroup.getDescription(),
                expenseGroup.getIcon(),
                expenseGroup.getParticipates().stream().map(participate -> new UserMemberExpenseGroupDTO(
                                participate.getUser().getId(),
                                participate.getUser().getName()))
                        .toList()
        );
    }

}
