package com.ferrinsa.fairpartner.expense.model;

import com.ferrinsa.fairpartner.expense.model.compositeid.PaymentId;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "payment")
public class Payment {

    @EmbeddedId
    private PaymentId idCompound;

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

    public Payment() {
        // Required by JPA
    }

    public Payment(UserEntity user, Expense expense, BigDecimal amount) {
        this.user = user;
        this.expense = expense;
        this.amount = amount;
    }

    public PaymentId getIdCompound() {
        return idCompound;
    }

    public void setIdCompound(PaymentId idCompound) {
        this.idCompound = idCompound;
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
        Payment payment = (Payment) o;
        return Objects.equals(idCompound, payment.idCompound);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idCompound);
    }
}
