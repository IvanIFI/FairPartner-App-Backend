package com.ferrinsa.fairpartner.expense.model;

import com.ferrinsa.fairpartner.user.model.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "settlement")
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_expense_group", nullable = false)
    private ExpenseGroup expenseGroup;
    @ManyToOne
    @JoinColumn(name = "from_id_user", nullable = false)
    private UserEntity fromUser;
    @ManyToOne
    @JoinColumn(name = "to_id_user", nullable = false)
    private UserEntity toUser;
    @Positive
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(name = "create_date", insertable = false, updatable = false, nullable = false)
    private LocalDateTime createDate;

    public Settlement() {
        // Required by JPA
    }

    public Settlement(ExpenseGroup expenseGroup, UserEntity fromUser, UserEntity toUser, BigDecimal amount) {
        this.expenseGroup = expenseGroup;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExpenseGroup getExpenseGroup() {
        return expenseGroup;
    }

    public void setExpenseGroup(ExpenseGroup expenseGroup) {
        this.expenseGroup = expenseGroup;
    }

    public UserEntity getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserEntity fromUser) {
        this.fromUser = fromUser;
    }

    public UserEntity getToUser() {
        return toUser;
    }

    public void setToUser(UserEntity toUser) {
        this.toUser = toUser;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settlement that = (Settlement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
