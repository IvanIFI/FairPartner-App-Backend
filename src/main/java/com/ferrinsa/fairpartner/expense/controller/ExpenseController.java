package com.ferrinsa.fairpartner.expense.controller;

import com.ferrinsa.fairpartner.expense.dto.expense.*;
import com.ferrinsa.fairpartner.expense.model.Expense;
import com.ferrinsa.fairpartner.expense.service.coordinator.ExpenseCoordinatorService;
import com.ferrinsa.fairpartner.expense.service.coordinator.model.ExpensesWithBalances;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Validated
@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseCoordinatorService expenseCoordinatorService;

    @Autowired
    public ExpenseController(ExpenseCoordinatorService expenseCoordinatorService) {
        this.expenseCoordinatorService = expenseCoordinatorService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/me/expenses")
    public ResponseEntity<ExpenseDetailsResponseDTO> createNewExpense(
            @AuthenticationPrincipal UserEntity authUser,
            @Valid @RequestBody CreateExpenseRequestDTO createExpenseRequestDTO) {
        Expense createExpense = expenseCoordinatorService.createExpense(authUser.getId(), createExpenseRequestDTO);

        URI locationNewExpense = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createExpense.getId())
                .toUri();

        return ResponseEntity.created(locationNewExpense).body(ExpenseDetailsResponseDTO.of(createExpense));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me/expenses/{expenseId}")
    public ResponseEntity<ExpenseDetailsResponseDTO> getExpense(
            @AuthenticationPrincipal UserEntity authUser,
            @PathVariable Long expenseId) {
        Expense expense = expenseCoordinatorService.getExpenseDetails(authUser.getId(), expenseId);
        return ResponseEntity.ok(ExpenseDetailsResponseDTO.of(expense));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me/expenses/expense-group/{expenseGroupId}")
    public ResponseEntity<ExpensesWithBalancesResponseDTO> getListExpenses(
            @AuthenticationPrincipal UserEntity authUser,
            @PathVariable Long expenseGroupId) {
        ExpensesWithBalances expensesWithBalances = expenseCoordinatorService.getListExpenses(
                authUser.getId(), expenseGroupId);
        return ResponseEntity.ok(ExpensesWithBalancesResponseDTO.of(expensesWithBalances));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/me/expenses/{expenseId}")
    public ResponseEntity<Void> deleteExpense(
            @AuthenticationPrincipal UserEntity authUser,
            @PathVariable Long expenseId) {
        expenseCoordinatorService.deleteExpense(authUser.getId(), expenseId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me/expenses/{expenseId}")
    public ResponseEntity<ExpenseDetailsResponseDTO> updateExpense(
            @AuthenticationPrincipal UserEntity authUser,
            @PathVariable Long expenseId,
            @Valid @RequestBody UpdateExpenseRequestDTO updateExpenseRequestDTO) {
        Expense expenseUpdated = expenseCoordinatorService.updateExpense(
                authUser.getId(),
                expenseId,
                updateExpenseRequestDTO);
        return ResponseEntity.ok(ExpenseDetailsResponseDTO.of(expenseUpdated));
    }

}
