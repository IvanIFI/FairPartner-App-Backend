package com.ferrinsa.fairpartner.expense.dto.invitation;

import com.ferrinsa.fairpartner.expense.model.Invitation;

import java.time.LocalDateTime;


public record InvitationSummaryResponseDTO(
        String expenseGroupName,
        String expenseGroupDescription,
        String UserInviterName,
        LocalDateTime expirationDate,
        String status
) {

    public static InvitationSummaryResponseDTO of (Invitation invitation){
        return new InvitationSummaryResponseDTO(
                invitation.getExpenseGroup().getName(),
                invitation.getExpenseGroup().getDescription(),
                invitation.getInviterUser().getName(),
                invitation.getExpirationDate(),
                invitation.getStatus().name()
        );
    }

}
