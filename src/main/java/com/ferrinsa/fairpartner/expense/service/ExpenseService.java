package com.ferrinsa.fairpartner.expense.service;

import com.ferrinsa.fairpartner.expense.dto.expense.ExpenseCreatedResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.expense.ExpenseCreateRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.expense.ExpenseResponseDTO;
import com.ferrinsa.fairpartner.user.model.UserEntity;

public interface ExpenseService {

    ExpenseResponseDTO getExpenseById(Long id);

    ExpenseCreatedResponseDTO createExpense(UserEntity authUser,
                                            ExpenseCreateRequestDTO expenseCreateRequestDTO);


    //TODO: metodos abstractos
    /*
    * Delete 204 sin body.
    * Update 200 + DTO actualizado (o 204 sin body).
    *
    * // FINDS: 200 + DTO (o 404 si no existe).
    * findByExpenseGroupId
    * findByName
    * findByCategoryId
    * findByDateBetween
    * findByCantBetween
    */
}
