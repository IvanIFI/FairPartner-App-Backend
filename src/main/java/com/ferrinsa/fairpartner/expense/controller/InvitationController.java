package com.ferrinsa.fairpartner.expense.controller;

import com.ferrinsa.fairpartner.expense.dto.invitation.CreateInvitationRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.invitation.InvitationDetailsResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.invitation.InvitationSummaryResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.invitation.InvitationTokenResponseDTO;
import com.ferrinsa.fairpartner.expense.service.InvitationService;
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
@RequestMapping("/me/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    @Autowired
    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/receive/{token}")
    public ResponseEntity<InvitationSummaryResponseDTO> getInvitationToInvitedUser(
            @AuthenticationPrincipal UserEntity authUser,
            @PathVariable String token) {
        return ResponseEntity.ok(invitationService.receiveInvitation(authUser.getId(), token));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<InvitationDetailsResponseDTO> getInvitationDetails(
            @AuthenticationPrincipal UserEntity authUser,
            @PathVariable Long id) {
        return ResponseEntity.ok(invitationService.findSentInvitationDetail(authUser.getId(), id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public ResponseEntity<List<InvitationSummaryResponseDTO>> getAllInvitationsByUser(
            @AuthenticationPrincipal UserEntity authUser) {
        return ResponseEntity.ok(invitationService.findAllSentInvitations(authUser.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    public ResponseEntity<InvitationTokenResponseDTO> createInvitation(
            @AuthenticationPrincipal UserEntity authUser,
            @Valid @RequestBody CreateInvitationRequestDTO createInvitationRequestDTO) {
        InvitationTokenResponseDTO invitationToken = invitationService.createInvitation(
                authUser.getId(),
                createInvitationRequestDTO
        );

        URI locationNewInvitation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(invitationToken.id())
                .toUri();
        return ResponseEntity.created(locationNewInvitation).body(invitationToken);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/receive/{token}/accept")
    public ResponseEntity<Void> acceptInvitation(
            @AuthenticationPrincipal UserEntity authUser,
            @PathVariable String token) {
        invitationService.acceptInvitation(authUser.getId(), token);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/receive/{token}/reject")
    public ResponseEntity<Void> rejectInvitation(
            @AuthenticationPrincipal UserEntity authUser,
            @PathVariable String token) {
        invitationService.rejectInvitation(authUser.getId(), token);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/sent/{id}/cancel")
    public ResponseEntity<Void> cancelInvitation(
            @AuthenticationPrincipal UserEntity authUser,
            @PathVariable Long id) {
        invitationService.cancelInvitation(authUser.getId(), id);
        return ResponseEntity.noContent().build();
    }

}
