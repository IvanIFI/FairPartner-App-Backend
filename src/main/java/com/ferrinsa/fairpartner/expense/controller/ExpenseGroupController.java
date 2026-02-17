package com.ferrinsa.fairpartner.expense.controller;

import com.ferrinsa.fairpartner.expense.dto.expensegroup.ExpenseGroupResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.expensegroup.NewExpenseGroupRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.expensegroup.UpdateExpenseGroupRequestDTO;
import com.ferrinsa.fairpartner.expense.service.ExpenseGroupService;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/expense-groups")
public class ExpenseGroupController {

    private final ExpenseGroupService expenseServiceGroup;

    @Autowired
    public ExpenseGroupController(ExpenseGroupService expenseServiceGroup) {
        this.expenseServiceGroup = expenseServiceGroup;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/me/groups")
    public ResponseEntity<ExpenseGroupResponseDTO> createNewExpenseGroup(
            @AuthenticationPrincipal UserEntity authUser,
            @Valid @RequestBody NewExpenseGroupRequestDTO newGroupRequestDTO) {
        ExpenseGroupResponseDTO createdGroup =
                expenseServiceGroup.createExpenseGroup(authUser.getId(), newGroupRequestDTO);

        URI locationNewGroup = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdGroup.id())
                .toUri();
        return ResponseEntity.created(locationNewGroup).body(createdGroup);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me/groups")
    public ResponseEntity<List<ExpenseGroupResponseDTO>> getExpenseGroups(
            @AuthenticationPrincipal UserEntity authUser) {
        return ResponseEntity.ok(expenseServiceGroup.findExpenseGroupsByUser(authUser.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me/groups/{groupId}")
    public ResponseEntity<ExpenseGroupResponseDTO> getExpenseGroup(
            @AuthenticationPrincipal UserEntity authUser,
            @PathVariable Long groupId) {
        return ResponseEntity.ok(expenseServiceGroup.findExpenseGroupById(authUser.getId(), groupId));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me/groups/{groupId}")
    public ResponseEntity<ExpenseGroupResponseDTO> updateExpenseGroup(
            @AuthenticationPrincipal UserEntity authUser,
            @PathVariable Long groupId,
            @Valid @RequestBody UpdateExpenseGroupRequestDTO updateExpenseGroupRequestDTO) {
        return ResponseEntity.ok(
                expenseServiceGroup.updateExpenseGroup(authUser.getId(), groupId, updateExpenseGroupRequestDTO));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/me/groups/{groupId}")
    public ResponseEntity<Void> deleteExpenseGroup(@AuthenticationPrincipal UserEntity authUser,
                                                   @PathVariable Long groupId) {
        expenseServiceGroup.leaveCurrentUserFromExpenseGroup(authUser.getId(), groupId);
        return ResponseEntity.noContent().build();
    }

}
