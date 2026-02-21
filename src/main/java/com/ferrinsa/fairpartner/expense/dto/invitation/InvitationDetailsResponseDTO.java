package com.ferrinsa.fairpartner.expense.dto.invitation;

import com.ferrinsa.fairpartner.expense.model.Invitation;

import java.time.LocalDateTime;

public record InvitationDetailsResponseDTO(
        Long id,
        Long expenseGroupId,
        Long inviterUserId,
        Long invitedUserId,
        String token,
        LocalDateTime creationDate,
        LocalDateTime expirationDate,
        String status
) {
    public static InvitationDetailsResponseDTO of(Invitation invitation) {
        return new InvitationDetailsResponseDTO(
                invitation.getId(),
                invitation.getExpenseGroup().getId(),
                invitation.getInviterUser().getId(),
                invitation.getInvitedUser().getId(),
                invitation.getToken(),
                invitation.getCreationDate(),
                invitation.getExpirationDate(),
                invitation.getStatus().name()
        );
    }
}
