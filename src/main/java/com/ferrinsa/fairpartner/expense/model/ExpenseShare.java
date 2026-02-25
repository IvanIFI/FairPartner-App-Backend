package com.ferrinsa.fairpartner.expense.model;

import com.ferrinsa.fairpartner.expense.model.compositeid.ExpenseShareId;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "expense_share")
public class ExpenseShare {

    @EmbeddedId
    private ExpenseShareId id;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserEntity user;

    @MapsId("expenseId")
    @ManyToOne
    @JoinColumn(name = "id_expense", nullable = false)
    private Expense expense;

    @Positive
    @Column(nullable = false)
    private BigDecimal amount;

    public ExpenseShare() {
        // Required by JPA
    }

    public ExpenseShare(UserEntity user, Expense expense, BigDecimal amount) {
        this.user = user;
        this.expense = expense;
        this.amount = amount;
    }

    public ExpenseShareId getId() {
        return id;
    }

    public void setId(ExpenseShareId id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseShare that = (ExpenseShare) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
