package com.ferrinsa.fairpartner.expense.service;

import com.ferrinsa.fairpartner.expense.dto.invitation.CreateInvitationRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.invitation.InvitationTokenResponseDTO;

public interface InvitationService {

    InvitationTokenResponseDTO createInvitation(Long authUserId,
                                                CreateInvitationRequestDTO createInvitationRequestDTO);

    void acceptInvitation(Long authUserId, String token);

    void rejectInvitation(Long authUserId, String token);

    void cancelInvitation(Long authUserId, Long invitationId);

}
