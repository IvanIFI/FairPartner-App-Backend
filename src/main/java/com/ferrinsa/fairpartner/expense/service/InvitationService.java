package com.ferrinsa.fairpartner.expense.service;

import com.ferrinsa.fairpartner.expense.dto.invitation.CreateInvitationRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.invitation.InvitationDetailsResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.invitation.InvitationSummaryResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.invitation.InvitationTokenResponseDTO;

import java.util.List;

public interface InvitationService {

    List<InvitationSummaryResponseDTO> findAllSentInvitations(Long authUserId);

    InvitationDetailsResponseDTO findSentInvitationDetail(Long authUserId, Long invitationId);

    InvitationTokenResponseDTO createInvitation(Long authUserId,
                                                CreateInvitationRequestDTO createInvitationRequestDTO);

    InvitationSummaryResponseDTO receiveInvitation(Long authUserId, String token);

    void acceptInvitation(Long authUserId, String token);

    void rejectInvitation(Long authUserId, String token);

    void cancelInvitation(Long authUserId, Long invitationId);

}
