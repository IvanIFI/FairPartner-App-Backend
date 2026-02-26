package com.ferrinsa.fairpartner.expense.service.domain.impl;

import com.ferrinsa.fairpartner.exception.expense.expense.ExpenseNotFoundException;
import com.ferrinsa.fairpartner.expense.model.Expense;
import com.ferrinsa.fairpartner.expense.repository.ExpenseRepository;
import com.ferrinsa.fairpartner.expense.service.domain.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    @Transactional
    public Expense createExpense(Expense newExpense) {
        expenseRepository.save(newExpense);
        return expenseRepository.findById(newExpense.getId())
                .orElseThrow(() -> new ExpenseNotFoundException(String.valueOf(newExpense.getId())));
    }

    @Override
    @Transactional
    public Expense udpateExpense(Long expenseId, Expense expenseNewValues) {
        Expense expenseUpdate = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException(String.valueOf(expenseId)));

        expenseUpdate.setName(expenseNewValues.getName());
        expenseUpdate.setDescription(expenseNewValues.getDescription());
        expenseUpdate.setCategory(expenseNewValues.getCategory());
        expenseUpdate.setAmount(expenseNewValues.getAmount());

        return expenseUpdate;
    }

    @Override
    @Transactional
    public void deleteExpense(Long expenseToDeleteId) {
        if (!expenseRepository.existsById(expenseToDeleteId)) {
            throw new ExpenseNotFoundException(String.valueOf(expenseToDeleteId));
        }
        expenseRepository.deleteById(expenseToDeleteId);
    }

    @Override
    public Expense findById(Long expenseId) {
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException(String.valueOf(expenseId)));
    }

    @Override
    public List<Expense> findByGroupId(Long expenseGroupId) {
        return expenseRepository.findByExpenseGroupId(expenseGroupId);
    }

    @Override
    public List<Expense> findByName(String name) {
        return expenseRepository.findByName(name);
    }

    @Override
    public List<Expense> findByCategoryId(Long categoryId) {
        return expenseRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByDateBetween(startDate, endDate);
    }

    @Override
    public List<Expense> findByAmountBetween(BigDecimal min, BigDecimal max) {
        return expenseRepository.findByAmountBetween(min, max);
    }

}
