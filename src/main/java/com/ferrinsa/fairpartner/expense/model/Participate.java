package com.ferrinsa.fairpartner.expense.model;

import com.ferrinsa.fairpartner.expense.model.compositeid.ParticipateId;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "participates")
public class Participate {

    @EmbeddedId
    private ParticipateId id;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "id_user",nullable = false)
    private UserEntity user;

    @MapsId("expenseGroupId")
    @ManyToOne
    @JoinColumn(name = "id_expense_group",nullable = false)
    private ExpenseGroup expenseGroup;

    public Participate() {
        // Required by JPA
    }

    public Participate(UserEntity user, ExpenseGroup expenseGroup) {
        this.user = user;
        this.expenseGroup = expenseGroup;
    }

    public ParticipateId getId() {
        return id;
    }

    public void setId(ParticipateId id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ExpenseGroup getExpenseGroup() {
        return expenseGroup;
    }

    public void setExpenseGroup(ExpenseGroup expenseGroup) {
        this.expenseGroup = expenseGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participate that = (Participate) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
