package com.ferrinsa.fairpartner.expense.controller;

import com.ferrinsa.fairpartner.expense.service.ExpenseGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/expense-groups")
public class ExpenseGroupController {

    private final ExpenseGroupService expenseServiceGroup;

    @Autowired
    public ExpenseGroupController(ExpenseGroupService expenseServiceGroup) {
        this.expenseServiceGroup = expenseServiceGroup;
    }
}
