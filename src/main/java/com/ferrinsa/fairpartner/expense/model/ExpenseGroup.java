package com.ferrinsa.fairpartner.expense.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "expense_group")
public class ExpenseGroup {

    //TODO: RECUERDA EL ATRIBUTO RESUME QUE NO SE ALMACENA SINO QUE SE CUALCULA EN LA LOGICA DE LA APLICACION

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @Column(nullable = false)
    private String icon;

    public ExpenseGroup() {
    }

    public ExpenseGroup(String description, String icon) {
        this.description = description;
        this.icon = icon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseGroup that = (ExpenseGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
