package com.ferrinsa.fairpartner.expense.repository;


import com.ferrinsa.fairpartner.expense.model.ExpenseGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseGroupRepository extends JpaRepository<ExpenseGroup,Long> {
}
