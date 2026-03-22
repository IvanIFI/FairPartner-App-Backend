package com.ferrinsa.fairpartner.expense.dto.invitation;

import com.ferrinsa.fairpartner.expense.model.Invitation;

public record InvitationTokenResponseDTO(
        Long id,
        String token
) {
    public static InvitationTokenResponseDTO of(Invitation invitation) {
        return new InvitationTokenResponseDTO(
                invitation.getId(),
                invitation.getToken()
        );
    }
}
