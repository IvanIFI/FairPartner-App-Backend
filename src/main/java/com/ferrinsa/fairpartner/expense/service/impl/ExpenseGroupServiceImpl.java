package com.ferrinsa.fairpartner.expense.service.impl;

import com.ferrinsa.fairpartner.expense.model.ExpenseGroup;
import com.ferrinsa.fairpartner.expense.repository.ExpenseGroupRepository;
import com.ferrinsa.fairpartner.expense.service.ExpenseGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseGroupServiceImpl implements ExpenseGroupService {

    private final ExpenseGroupRepository expenseGroupRepository;

    @Autowired
    public ExpenseGroupServiceImpl(ExpenseGroupRepository expenseGroupRepository) {
        this.expenseGroupRepository = expenseGroupRepository;
    }

    @Override
    public List<ExpenseGroup> findAll() {
        return List.of();
    }
}
