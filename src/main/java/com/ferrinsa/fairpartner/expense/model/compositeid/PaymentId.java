package com.ferrinsa.fairpartner.expense.model.compositeid;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PaymentId implements Serializable {

    private Long userId;
    private Long expenseId;

    public PaymentId() {
        // Required by JPA
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
        PaymentId that = (PaymentId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(expenseId, that.expenseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, expenseId);
    }
}
