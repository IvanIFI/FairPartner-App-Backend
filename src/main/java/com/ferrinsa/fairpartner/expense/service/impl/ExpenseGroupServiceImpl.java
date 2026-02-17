package com.ferrinsa.fairpartner.expense.service.impl;

import com.ferrinsa.fairpartner.exception.expense.ExpenseGroupAccessDeniedException;
import com.ferrinsa.fairpartner.exception.expense.ExpenseGroupNotFoundException;
import com.ferrinsa.fairpartner.exception.expense.ParticipationAlreadyExistsException;
import com.ferrinsa.fairpartner.expense.dto.expensegroup.ExpenseGroupResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.expensegroup.NewExpenseGroupRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.expensegroup.UpdateExpenseGroupRequestDTO;
import com.ferrinsa.fairpartner.expense.model.ExpenseGroup;
import com.ferrinsa.fairpartner.expense.model.Participate;
import com.ferrinsa.fairpartner.expense.repository.ExpenseGroupRepository;
import com.ferrinsa.fairpartner.expense.repository.ParticipateRepository;
import com.ferrinsa.fairpartner.expense.service.ExpenseGroupService;
import com.ferrinsa.fairpartner.user.repository.UserRepository;
import com.ferrinsa.fairpartner.utils.CheckUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExpenseGroupServiceImpl implements ExpenseGroupService {

    private final ExpenseGroupRepository expenseGroupRepository;
    private final ParticipateRepository participateRepository;
    private final UserRepository userRepository;

    @Autowired
    public ExpenseGroupServiceImpl(ExpenseGroupRepository expenseGroupRepository,
                                   ParticipateRepository participateRepository,
                                   UserRepository userRepository) {
        this.expenseGroupRepository = expenseGroupRepository;
        this.participateRepository = participateRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ExpenseGroupResponseDTO> findExpenseGroupsByUser(Long authUserId) {
        List<ExpenseGroup> groupsByCurrentUser = participateRepository
                .findExpenseGroupsWithUsersByUserId(authUserId);
        return groupsByCurrentUser.stream().map(ExpenseGroupResponseDTO::of).toList();
    }

    @Override
    public ExpenseGroupResponseDTO findExpenseGroupById(Long authUserId, Long expenseGroupId) {

        ExpenseGroup expenseGroupFound = expenseGroupRepository.findById(expenseGroupId)
                .orElseThrow(() -> new ExpenseGroupNotFoundException(String.valueOf(expenseGroupId)));

        boolean existsParticipationUserInGroup = participateRepository.existsByUserIdAndExpenseGroupId(
                authUserId,
                expenseGroupId);

        if (!existsParticipationUserInGroup) {
            throw new ExpenseGroupAccessDeniedException(String.valueOf(authUserId), String.valueOf(expenseGroupId));
        }

        return ExpenseGroupResponseDTO.of(expenseGroupFound);
    }

    @Override
    @Transactional
    public ExpenseGroupResponseDTO createExpenseGroup(Long authUserId,
                                                      NewExpenseGroupRequestDTO newExpenseGroupRequestDTO) {
        ExpenseGroup newExpenseGroup = new ExpenseGroup(
                newExpenseGroupRequestDTO.name(),
                newExpenseGroupRequestDTO.description(),
                newExpenseGroupRequestDTO.icon()
        );
        expenseGroupRepository.save(newExpenseGroup);

        Participate newParticipate = new Participate(userRepository.getReferenceById(authUserId), newExpenseGroup);
        newExpenseGroup.addParticipate(newParticipate);
        participateRepository.save(newParticipate);

        return ExpenseGroupResponseDTO.of(newExpenseGroup);
    }

    @Override
    @Transactional
    public ExpenseGroupResponseDTO addUserToExpenseGroup(Long authUserId, Long expenseGroupId) {
        boolean existsParticipation = participateRepository.existsByUserIdAndExpenseGroupId(
                authUserId,
                expenseGroupId);

        if (existsParticipation) {
            throw new ParticipationAlreadyExistsException(String.valueOf(
                    authUserId),
                    String.valueOf(expenseGroupId));
        }

        ExpenseGroup expenseGroup = expenseGroupRepository.findById(expenseGroupId).orElseThrow(
                () -> new ExpenseGroupNotFoundException(String.valueOf(expenseGroupId)));
        Participate newParticipate = new Participate(userRepository.getReferenceById(authUserId), expenseGroup);

        participateRepository.save(newParticipate);
        expenseGroup.addParticipate(newParticipate);

        return ExpenseGroupResponseDTO.of(expenseGroup);
    }

    @Override
    @Transactional
    public void leaveCurrentUserFromExpenseGroup(Long authUserId, Long expenseGroupId) {
        participateRepository.deleteByUserIdAndExpenseGroupId(authUserId, expenseGroupId);
        participateRepository.flush();
        checkAndDeleteExpenseGroup(expenseGroupId);
    }

    @Override
    @Transactional
    public ExpenseGroupResponseDTO updateExpenseGroup(Long authUserId,
                                                      Long expenseGroupId,
                                                      UpdateExpenseGroupRequestDTO updateExpenseGroupRequestDTO) {
        ExpenseGroup expenseGroupToUpdate = expenseGroupRepository.findById(expenseGroupId)
                .orElseThrow(() -> new ExpenseGroupNotFoundException(String.valueOf(expenseGroupId)));

        boolean existsParticipationUserInGroup = participateRepository.existsByUserIdAndExpenseGroupId(
                authUserId,
                expenseGroupId);

        if (!existsParticipationUserInGroup) {
            throw new ExpenseGroupAccessDeniedException(String.valueOf(authUserId), String.valueOf(expenseGroupId));
        }

        checkFieldsToUpdateExpenseGroup(expenseGroupToUpdate, updateExpenseGroupRequestDTO);

        return ExpenseGroupResponseDTO.of(expenseGroupToUpdate);
    }

    private void checkAndDeleteExpenseGroup(Long expenseGroupId) {
        ExpenseGroup expenseGroupToCheck = expenseGroupRepository.findById(expenseGroupId)
                .orElseThrow(() -> new ExpenseGroupNotFoundException(Long.toString(expenseGroupId)));

        if (expenseGroupToCheck.getParticipates().isEmpty()) {
            expenseGroupRepository.delete(expenseGroupToCheck);
        }
    }

    private void checkFieldsToUpdateExpenseGroup(ExpenseGroup expenseGroupToUpdate, UpdateExpenseGroupRequestDTO updateExpenseGroupRequestDTO) {
        if (updateExpenseGroupRequestDTO.name() != null) {
            CheckUtils.isValidSize("name", updateExpenseGroupRequestDTO.name(), 20);
            expenseGroupToUpdate.setName(updateExpenseGroupRequestDTO.name());
        }

        if (updateExpenseGroupRequestDTO.description() != null) {
            CheckUtils.isValidSize("description", updateExpenseGroupRequestDTO.description(), 200);
            expenseGroupToUpdate.setDescription(updateExpenseGroupRequestDTO.description());
        }

        if (updateExpenseGroupRequestDTO.icon() != null) {
            CheckUtils.isValidSize("icon", updateExpenseGroupRequestDTO.icon(), 300);
            expenseGroupToUpdate.setIcon(updateExpenseGroupRequestDTO.icon());
        }
    }

}


