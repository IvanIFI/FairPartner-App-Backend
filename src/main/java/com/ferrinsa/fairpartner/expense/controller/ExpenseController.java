package com.ferrinsa.fairpartner.expense.controller;

import com.ferrinsa.fairpartner.expense.dto.expense.ExpenseCreateRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.expense.ExpenseCreatedResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.expense.ExpenseResponseDTO;
import com.ferrinsa.fairpartner.expense.service.ExpenseService;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Validated
@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ExpenseResponseDTO getExpenseById(@PathVariable Long id){
        return expenseService.getExpenseById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ExpenseCreatedResponseDTO> createExpense(@AuthenticationPrincipal UserEntity authUser,
                                                                   @Valid @RequestBody
                                                                   ExpenseCreateRequestDTO expenseCreateRequestDTO) {

        ExpenseCreatedResponseDTO expenseCreated = expenseService.createExpense(authUser,expenseCreateRequestDTO);
        URI location = URI.create(expenseCreated.resourceUrl());

        return ResponseEntity.created(location).body(expenseCreated);
    }
}
