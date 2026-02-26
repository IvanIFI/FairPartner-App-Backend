package com.ferrinsa.fairpartner.expense.service.domain.impl;

import com.ferrinsa.fairpartner.exception.expenseshare.ExpenseShareNotFoundException;
import com.ferrinsa.fairpartner.expense.model.ExpenseShare;
import com.ferrinsa.fairpartner.expense.repository.ExpenseShareRepository;
import com.ferrinsa.fairpartner.expense.service.domain.ExpenseShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExpenseShareServiceImpl implements ExpenseShareService {

    ExpenseShareRepository expenseShareRepository;

    @Autowired
    public ExpenseShareServiceImpl(ExpenseShareRepository expenseShareRepository) {
        this.expenseShareRepository = expenseShareRepository;
    }

    @Override
    @Transactional
    public ExpenseShare createExpenseShare(ExpenseShare newExpenseShare) {
        expenseShareRepository.save(newExpenseShare);
        return newExpenseShare;
    }

    /*
     * The user and expense are not updated here because they are part of the composite primary key.
     * Changing either of them would require modifying the primary key, which is not supported
     * by JPA for managed entities.
     *
     * If the distribution structure needs to change (e.g., different users or reassignment),
     * this must be handled at the Coordinator level by deleting the existing ExpenseShare
     * records and creating new ones.
     */
    @Override
    @Transactional
    public ExpenseShare updateExpenseShare(Long expenseId, Long userId, BigDecimal newAmount) {
        ExpenseShare expenseShareToUpdate = expenseShareRepository.findByExpenseIdAndUserId(expenseId, userId)
                .orElseThrow(ExpenseShareNotFoundException::new);

        expenseShareToUpdate.setAmount(newAmount);
        return expenseShareToUpdate;
    }

    @Override
    @Transactional
    public void deleteByExpenseId(Long expenseId) {
        expenseShareRepository.deleteByExpenseId(expenseId);
    }

    @Override
    public List<ExpenseShare> findByExpenseId(Long expenseId) {
        return expenseShareRepository.findByExpenseId(expenseId);
    }

}
