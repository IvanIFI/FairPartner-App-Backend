package com.ferrinsa.fairpartner.expense.service.impl;

import com.ferrinsa.fairpartner.category.model.CategoryEntity;
import com.ferrinsa.fairpartner.category.repository.CategoryEntityRepository;
import com.ferrinsa.fairpartner.exception.category.CategoryNotFoundException;
import com.ferrinsa.fairpartner.exception.expense.expensegroup.ExpenseGroupNotFoundException;
import com.ferrinsa.fairpartner.exception.expense.expense.ExpenseNotFoundException;
import com.ferrinsa.fairpartner.expense.dto.expense.ExpenseCreateRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.expense.ExpenseCreatedResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.expense.ExpenseResponseDTO;
import com.ferrinsa.fairpartner.expense.model.Expense;
import com.ferrinsa.fairpartner.expense.model.ExpenseGroup;
import com.ferrinsa.fairpartner.expense.repository.ExpenseGroupRepository;
import com.ferrinsa.fairpartner.expense.repository.ExpenseRepository;
import com.ferrinsa.fairpartner.expense.service.ExpenseService;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    //FIXME: MOVER Y CREAR UNA CLASE DE CONSTANTES? APPCONSTATS
    private static final String DEFAULT_ICON = "icon";

    private final ExpenseRepository expenseRepository;
    private final ExpenseGroupRepository expenseGroupRepository;
    private final CategoryEntityRepository categoryEntityRepository;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository, ExpenseGroupRepository expenseGroupRepository, CategoryEntityRepository categoryEntityRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseGroupRepository = expenseGroupRepository;
        this.categoryEntityRepository = categoryEntityRepository;
    }

    @Transactional
    @Override
    public ExpenseCreatedResponseDTO createExpense(UserEntity authUser,
                                                   ExpenseCreateRequestDTO expenseCreateRequestDTO) {
        ExpenseGroup expenseGroup = getExpenseGroupOrThrow(expenseCreateRequestDTO.expenseGroupId());
        CategoryEntity category = getCategoryOrThrow(expenseCreateRequestDTO.categoryId());
        String icon = checkIconUserOrDefault(expenseCreateRequestDTO.icon());

        Expense expense = new Expense(
                expenseGroup,
                category,
                authUser,
                expenseCreateRequestDTO.name(),
                expenseCreateRequestDTO.description(),
                expenseCreateRequestDTO.cant(),
                icon
        );

        expenseRepository.save(expense);

        return ExpenseCreatedResponseDTO.of(expense);
    }

    @Override
    public ExpenseResponseDTO getExpenseById(Long id) {
        Expense expense = getExpenseOrThrow(id);

        return ExpenseResponseDTO.of(expense);
    }







    private Expense getExpenseOrThrow(Long id){
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(Long.toString(id)));
    }

    private ExpenseGroup getExpenseGroupOrThrow(Long id) {
        return expenseGroupRepository.findById(id)
                .orElseThrow(() -> new ExpenseGroupNotFoundException(id.toString()));
    }

    private CategoryEntity getCategoryOrThrow(Long id) {
        return categoryEntityRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id.toString()));
    }

    private String checkIconUserOrDefault(String icon) {
        return (icon == null || icon.isBlank()) ? DEFAULT_ICON : icon;
    }

}
