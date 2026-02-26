package com.ferrinsa.fairpartner.expense.model;

import com.ferrinsa.fairpartner.category.model.CategoryEntity;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "expense")
public class Expense {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_expense_group", nullable = false)
    private ExpenseGroup expenseGroup;
    @ManyToOne
    @JoinColumn(name = "id_category", nullable = false)
    private CategoryEntity category;
    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private UserEntity createdBy;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(insertable = false, updatable = false, nullable = false)
    private LocalDate createdDate;
    @Column(nullable = false)
    private String icon;
    @Positive
    @Column(nullable = false)
    private BigDecimal amount;

    public Expense() {
        // Required by JPA
    }

    public Expense(ExpenseGroup expenseGroup,
                   CategoryEntity category,
                   UserEntity createdBy,
                   String name,
                   String description,
                   BigDecimal amount,
                   String icon) {
        this.expenseGroup = expenseGroup;
        this.category = category;
        this.createdBy = createdBy;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.icon = icon;
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

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UserEntity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserEntity createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Objects.equals(id, expense.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
