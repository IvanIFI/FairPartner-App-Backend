package com.ferrinsa.fairpartner.expense.model;

import com.ferrinsa.fairpartner.category.model.CategoryEntity;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

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
    @JoinColumn(name = "id_user", nullable = false)
    private UserEntity createdBy;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(insertable = false, updatable = false, nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private String icon;
    @Column(nullable = false)
    @Positive
    private Double cant;

    public Expense() {
        // Required by JPA
    }

    public Expense(ExpenseGroup expenseGroup, CategoryEntity category,UserEntity createdBy, String name, String description, Double cant, String icon) {
        this.expenseGroup = expenseGroup;
        this.category = category;
        this.createdBy = createdBy;
        this.name = name;
        this.description = description;
        this.cant = cant;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public @Positive Double getCant() {
        return cant;
    }

    public void setCant(@Positive Double cant) {
        this.cant = cant;
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
