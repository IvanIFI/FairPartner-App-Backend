package com.ferrinsa.fairpartner.expense.model.compositeid;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ParticipateId implements Serializable {

    private Long userId;
    private Long expenseGroupId;

    public ParticipateId() {
        // Required by JPA
    }

    public ParticipateId(Long userId, Long expenseGroupId) {
        this.userId = userId;
        this.expenseGroupId = expenseGroupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getExpenseGroupId() {
        return expenseGroupId;
    }

    public void setExpenseGroupId(Long expenseGroupId) {
        this.expenseGroupId = expenseGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipateId that = (ParticipateId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(expenseGroupId, that.expenseGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, expenseGroupId);
    }
}
