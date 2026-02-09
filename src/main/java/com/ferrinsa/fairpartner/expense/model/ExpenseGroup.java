package com.ferrinsa.fairpartner.expense.model;

import com.ferrinsa.fairpartner.user.model.UserEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "expense_group")
public class ExpenseGroup {

    //TODO: RECUERDA EL ATRIBUTO RESUME QUE NO SE ALMACENA SINO QUE SE CUALCULA EN LA LOGICA DE LA APLICACION

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private String icon;
    @OneToMany(mappedBy = "expenseGroup")
    private List<Participate> participates = new ArrayList<>();

    public ExpenseGroup() {
        // Required by JPA
    }

    public ExpenseGroup(String name, String description, String icon) {
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Participate> getParticipates() {
        return participates;
    }

    public void setParticipates(List<Participate> participates) {
        this.participates = participates;
    }

    public void addParticipate(Participate participate){
        this.participates.add(participate);
        participate.setExpenseGroup(this); // Keep bidirectional relationship consistent in memory
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
