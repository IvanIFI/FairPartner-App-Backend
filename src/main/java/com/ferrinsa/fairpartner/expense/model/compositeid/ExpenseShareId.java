package com.ferrinsa.fairpartner.expense.model.compositeid;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ExpenseShareId implements Serializable {

    private Long userId;
    private Long expenseId;

    public ExpenseShareId() {
        // Required by JPA
    }

    public ExpenseShareId(Long userId, Long expenseId) {
        this.userId = userId;
        this.expenseId = expenseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseShareId that = (ExpenseShareId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(expenseId, that.expenseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, expenseId);
    }
}
