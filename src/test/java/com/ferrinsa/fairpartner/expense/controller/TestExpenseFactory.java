package com.ferrinsa.fairpartner.expense.controller;

import com.ferrinsa.fairpartner.category.model.CategoryEntity;
import com.ferrinsa.fairpartner.expense.model.*;
import com.ferrinsa.fairpartner.user.model.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestExpenseFactory {

    public static Expense expense() {
        ExpenseGroup group = new ExpenseGroup();
        group.setId(1L);
        group.setName("Group Test");

        CategoryEntity category = new CategoryEntity();
        category.setId(1L);
        category.setName("Food");

        UserEntity creator = user();

        Expense expense = new Expense(
                group,
                category,
                creator,
                "Dinner",
                "Test expense",
                BigDecimal.valueOf(120)
        );

        expense.setId(1L);
        expense.setCreatedDate(LocalDate.now());

        return expense;
    }

    public static ExpenseShare share() {
        return new ExpenseShare(
                user(),
                expense(),
                BigDecimal.valueOf(60)
        );
    }

    public static UserEntity user() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("ivan@example.com");
        user.setName("Ivan");
        return user;
    }

    public static String CREATE_JSON = """
            {
              "expenseGroupId": 1,
              "categoryId": 1,
              "payerUserId": 1,
              "name": "Dinner",
              "description": "Test",
              "amount": 120,
              "expenseShares": [
                { "userId": 1, "amount": 120 }
              ]
            }
            """;

    public static String UPDATE_JSON = """
            {
              "categoryId": 1,
              "payerUserId": 1,
              "name": "Updated",
              "description": "Updated desc",
              "amount": 100,
              "expenseShares": [
                { "userId": 1, "amount": 100 }
              ]
            }
            """;
}
