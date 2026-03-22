package com.ferrinsa.fairpartner.expense.service.domain;

import com.ferrinsa.fairpartner.expense.dto.invitation.CreateInvitationRequestDTO;
import com.ferrinsa.fairpartner.expense.model.Invitation;

import java.util.List;

public interface InvitationService {

    List<Invitation> findAllSentInvitations(Long authUserId);

    Invitation findSentInvitationDetail(Long authUserId, Long invitationId);

    Invitation createInvitation(Long authUserId,
                                                CreateInvitationRequestDTO createInvitationRequestDTO);

    Invitation receiveInvitation(Long authUserId, String token);

    void acceptInvitation(Long authUserId, String token);

    void rejectInvitation(Long authUserId, String token);

    void cancelInvitation(Long authUserId, Long invitationId);

}
