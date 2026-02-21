package com.ferrinsa.fairpartner.expense.service.impl;

import com.ferrinsa.fairpartner.exception.expense.expensegroup.ExpenseGroupNotFoundException;
import com.ferrinsa.fairpartner.exception.expense.expensegroup.ParticipationAlreadyExistsException;
import com.ferrinsa.fairpartner.exception.expense.expensegroup.UserNotMemberOfGroupException;
import com.ferrinsa.fairpartner.exception.expense.invitation.*;
import com.ferrinsa.fairpartner.exception.user.UserNotFoundException;
import com.ferrinsa.fairpartner.expense.dto.invitation.CreateInvitationRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.invitation.InvitationDetailsResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.invitation.InvitationSummaryResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.invitation.InvitationTokenResponseDTO;
import com.ferrinsa.fairpartner.expense.model.ExpenseGroup;
import com.ferrinsa.fairpartner.expense.model.Invitation;
import com.ferrinsa.fairpartner.expense.model.Participate;
import com.ferrinsa.fairpartner.expense.repository.ExpenseGroupRepository;
import com.ferrinsa.fairpartner.expense.repository.InvitationRepository;
import com.ferrinsa.fairpartner.expense.repository.ParticipateRepository;
import com.ferrinsa.fairpartner.expense.service.InvitationService;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

//TODO: POSIBLE LOG EN EL REINTENTO DEL TOKEN?
@Service
public class InvitationServiceImpl implements InvitationService {

    // Single instance reused for token generation
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private final Long maxParticipants;

    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final ExpenseGroupRepository expenseGroupRepository;
    private final ParticipateRepository participateRepository;

    @Autowired
    public InvitationServiceImpl(UserRepository userRepository,
                                 InvitationRepository invitationRepository,
                                 ExpenseGroupRepository expenseGroupRepository,
                                 ParticipateRepository participateRepository,
                                 @Value("${app.domain.group-max-participants}") Long maxParticipants) {
        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
        this.expenseGroupRepository = expenseGroupRepository;
        this.participateRepository = participateRepository;
        this.maxParticipants = maxParticipants;
    }

    @Override
    @Transactional
    public List<InvitationSummaryResponseDTO> findAllSentInvitations(Long authUserId) {
        return invitationRepository.findByInviterUser_IdOrderByCreationDateDesc(authUserId)
                .stream()
                .map(inv ->
                        {
                            this.checkOrUpdateExpiredStatus(inv);
                            return InvitationSummaryResponseDTO.of(inv);
                        }
                ).toList();
    }

    @Override
    @Transactional
    public InvitationDetailsResponseDTO findSentInvitationDetail(Long authUserId, Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(InvitationNotFoundException::new);

        this.validateUserInviter(invitation, authUserId);
        this.checkOrUpdateExpiredStatus(invitation);

        return InvitationDetailsResponseDTO.of(invitation);
    }

    @Override
    @Transactional
    public InvitationTokenResponseDTO createInvitation(Long authUserId,
                                                       CreateInvitationRequestDTO createInvitationRequestDTO) {
        UserEntity inviterUser = userRepository.findById(authUserId)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(authUserId)));

        UserEntity invitedUser = userRepository.findByEmail(createInvitationRequestDTO.email())
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(createInvitationRequestDTO.email())));

        ExpenseGroup expenseGroup = expenseGroupRepository.findById(createInvitationRequestDTO.expenseGroupId())
                .orElseThrow(() -> new ExpenseGroupNotFoundException(
                        String.valueOf(createInvitationRequestDTO.expenseGroupId())));

        this.validateMaxUsersParticipate(createInvitationRequestDTO.expenseGroupId());
        this.checkInvitationToCreate(expenseGroup, inviterUser, invitedUser);

        LocalDateTime nowTime = LocalDateTime.now();
        Invitation invitation = new Invitation(
                expenseGroup,
                inviterUser,
                invitedUser,
                this.generateToken(),
                nowTime,
                nowTime.plusHours(24));

        // Ensures the generated token is unique before persisting it and manages the exception
        for (int i = 0; i < 3; i++) {
            try {
                invitationRepository.save(invitation);
                return new InvitationTokenResponseDTO(invitation.getId(), invitation.getToken());
            } catch (DataIntegrityViolationException ex) {
                invitation.setToken(generateToken());
            }
        }
        throw new InvitationCreationException();
    }

    @Override
    @Transactional
    public InvitationSummaryResponseDTO receiveInvitation(Long authUserId, String token) {
        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(InvitationNotFoundException::new);

        this.validateUserInvited(invitation, authUserId);
        this.checkOrUpdateExpiredStatus(invitation);

        return InvitationSummaryResponseDTO.of(invitation);
    }

    @Override
    @Transactional(noRollbackFor = ExpiredInvitationException.class)
    public void acceptInvitation(Long authUserId, String token) {
        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(InvitationNotFoundException::new);

        this.validateUserInvited(invitation, authUserId);
        this.checkOrUpdateExpiredStatus(invitation);
        this.validateInvitationStatusIsSent(invitation, authUserId);
        this.validateMaxUsersParticipate(invitation.getExpenseGroup().getId());
        this.validateNotAlreadyParticipant(invitation, authUserId);

        Participate newparticipate = new Participate(
                userRepository.getReferenceById(authUserId),
                invitation.getExpenseGroup());

        participateRepository.save(newparticipate);
        invitation.getExpenseGroup().addParticipate(newparticipate);

        invitation.setStatus(Invitation.InvitationStatus.ACCEPTED);
    }

    @Override
    @Transactional(noRollbackFor = ExpiredInvitationException.class)
    public void rejectInvitation(Long authUserId, String token) {
        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(InvitationNotFoundException::new);

        this.validateUserInvited(invitation, authUserId);
        this.checkOrUpdateExpiredStatus(invitation);
        this.validateInvitationStatusIsSent(invitation, authUserId);

        invitation.setStatus(Invitation.InvitationStatus.REJECTED);
    }

    @Override
    @Transactional(noRollbackFor = ExpiredInvitationException.class)
    public void cancelInvitation(Long authUserId, Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(InvitationNotFoundException::new);

        this.validateUserInviter(invitation, authUserId);
        this.checkOrUpdateExpiredStatus(invitation);
        this.validateInvitationStatusIsSent(invitation, authUserId);

        invitation.setStatus(Invitation.InvitationStatus.CANCELED);
    }

    private void checkInvitationToCreate(ExpenseGroup expenseGroup,
                                         UserEntity inviterUser,
                                         UserEntity invitedUser) {
        if (inviterUser.equals(invitedUser)) {
            throw new SelfInvitationNotAllowedException();
        }

        if (!participateRepository.existsByUserIdAndExpenseGroupId(inviterUser.getId(), expenseGroup.getId())) {
            throw new UserNotMemberOfGroupException(
                    String.valueOf(inviterUser.getId()),
                    String.valueOf(expenseGroup.getId()));
        }

        if (invitationRepository.existsByExpenseGroupAndInvitedUserAndStatus(
                expenseGroup, invitedUser, Invitation.InvitationStatus.SENT)) {
            throw new InvitationAlreadyExistsException(
                    String.valueOf(invitedUser.getId()),
                    String.valueOf(expenseGroup.getId()));
        }

        if (invitationRepository.existsByExpenseGroupAndInvitedUserAndStatus(
                expenseGroup, invitedUser, Invitation.InvitationStatus.ACCEPTED)) {
            throw new InvitationAlreadyAcceptedException(
                    String.valueOf(invitedUser.getId()),
                    String.valueOf(expenseGroup.getId()));
        }

        if (participateRepository.existsByUserIdAndExpenseGroupId(invitedUser.getId(), expenseGroup.getId())) {
            throw new ParticipationAlreadyExistsException(
                    String.valueOf(invitedUser.getId()), String.valueOf(expenseGroup.getId()));
        }
    }

    private void checkOrUpdateExpiredStatus(Invitation inv) {
        LocalDateTime now = LocalDateTime.now();
        if (inv.getExpirationDate().isBefore(now) && inv.getStatus() == Invitation.InvitationStatus.SENT) {
            inv.setStatus(Invitation.InvitationStatus.EXPIRED);
        }
    }

    private void validateInvitationStatusIsSent(Invitation invitation, Long authUserId) {
        if (invitation.getStatus() != Invitation.InvitationStatus.SENT) {
            switch (invitation.getStatus()) {
                case ACCEPTED -> throw new InvitationAlreadyAcceptedException(
                        String.valueOf(authUserId), String.valueOf(invitation.getExpenseGroup().getId()));
                case CANCELED -> throw new CanceledInvitationException();
                case REJECTED -> throw new RejectInvitationException();
                case EXPIRED -> throw new ExpiredInvitationException();
                default -> throw new InvitationStateNotManagedException();
            }
        }
    }

    private void validateNotAlreadyParticipant(Invitation invitation, Long authUserId) {
        if (participateRepository.existsByUserIdAndExpenseGroupId(authUserId, invitation.getExpenseGroup().getId())) {
            throw new ParticipationAlreadyExistsException(
                    String.valueOf(authUserId), String.valueOf(invitation.getExpenseGroup().getId()));
        }
    }

    private void validateUserInvited(Invitation invitation, Long authUserId) {
        if (!invitation.getInvitedUser().equals(userRepository.getReferenceById(authUserId))) {
            throw new UserNotInvitedException(String.valueOf(authUserId), String.valueOf(invitation.getId()));
        }
    }

    private void validateUserInviter(Invitation invitation, Long authUserId) {
        if (!invitation.getInviterUser().equals(userRepository.getReferenceById(authUserId))) {
            throw new UserNotInvitedException(String.valueOf(authUserId), String.valueOf(invitation.getId()));
        }
    }

    private void validateMaxUsersParticipate(long expenseGroupId) {
        long userMembersParticipates = participateRepository
                .countByExpenseGroup_Id(expenseGroupId);

        if (userMembersParticipates >= maxParticipants) {
           throw new MaxParticipantsExceededException();
        }
    }

    private String generateToken() {
        byte[] randomBytes = new byte[32];
        SECURE_RANDOM.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

}
