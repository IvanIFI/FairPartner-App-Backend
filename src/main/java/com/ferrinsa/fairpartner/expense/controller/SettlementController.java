package com.ferrinsa.fairpartner.expense.controller;

import com.ferrinsa.fairpartner.expense.dto.settlement.NewSettlementRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.settlement.SettlementResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.settlement.UpdateSettlementRequestDTO;
import com.ferrinsa.fairpartner.expense.model.Settlement;
import com.ferrinsa.fairpartner.expense.service.domain.SettlementService;
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
@RequestMapping("/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    @Autowired
    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/me")
    public ResponseEntity<SettlementResponseDTO> createSettlement(
            @AuthenticationPrincipal UserEntity authUser,
            @Valid @RequestBody NewSettlementRequestDTO newSettlementRequestDTO) {
        Settlement createdSettlement = settlementService.createSettlement(authUser.getId(), newSettlementRequestDTO);

        URI locationNewSettlement = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdSettlement.getId())
                .toUri();

        return ResponseEntity.created(locationNewSettlement).body(SettlementResponseDTO.of(createdSettlement));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me/{settlementId}")
    public ResponseEntity<SettlementResponseDTO> getSettlement(
            @AuthenticationPrincipal UserEntity authUser,
            @PathVariable Long settlementId) {
        Settlement settlement = settlementService.getSettlementById(authUser.getId(), settlementId);
        return ResponseEntity.ok(SettlementResponseDTO.of(settlement));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me/group/{expenseGroupId}")
    public ResponseEntity<List<SettlementResponseDTO>> getSettlementsByGroup(
            @AuthenticationPrincipal UserEntity authUser,
            @PathVariable Long expenseGroupId) {
        List<Settlement> settlements = settlementService.findAllByGroupId(
                authUser.getId(), expenseGroupId);

        return ResponseEntity.ok(settlements.stream()
                .map(SettlementResponseDTO::of)
                .toList());
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me/{settlementId}")
    public ResponseEntity<SettlementResponseDTO> updateSettlement(
            @AuthenticationPrincipal UserEntity authUser,
            @PathVariable Long settlementId,
            @Valid @RequestBody UpdateSettlementRequestDTO updateSettlementRequestDTO) {
        Settlement settlementToUpdate = settlementService.updateSettlement(
                authUser.getId(),
                settlementId,
                updateSettlementRequestDTO);
        return ResponseEntity.ok(SettlementResponseDTO.of(settlementToUpdate));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/me/{settlementId}")
    public ResponseEntity<Void> deleteSettlement(
            @AuthenticationPrincipal UserEntity authUser,
            @PathVariable Long settlementId) {
        settlementService.deleteSettlement(authUser.getId(), settlementId);
        return ResponseEntity.noContent().build();
    }

}
