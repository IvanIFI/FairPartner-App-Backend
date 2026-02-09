package com.ferrinsa.fairpartner.expense.service.impl;

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
import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.utils.CheckUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExpenseGroupServiceImpl implements ExpenseGroupService {

    private final ExpenseGroupRepository expenseGroupRepository;
    private final ParticipateRepository participateRepository;

    @Autowired
    public ExpenseGroupServiceImpl(ExpenseGroupRepository expenseGroupRepository,
                                   ParticipateRepository participateRepository) {
        this.expenseGroupRepository = expenseGroupRepository;
        this.participateRepository = participateRepository;
    }

    @Override
    public List<ExpenseGroupResponseDTO> findExpenseGroupsByUser(UserEntity authUser) {
        List<ExpenseGroup> groupsByCurrentUser = participateRepository
                .findExpenseGroupsWithUsersByUserId(authUser.getId());
        return groupsByCurrentUser.stream().map(ExpenseGroupResponseDTO::of).toList();
    }

    @Override
    @Transactional
    public ExpenseGroupResponseDTO createExpenseGroup(UserEntity authUser,
                                                      NewExpenseGroupRequestDTO newExpenseGroupRequestDTO) {
        ExpenseGroup newExpenseGroup = new ExpenseGroup(
                newExpenseGroupRequestDTO.name(),
                newExpenseGroupRequestDTO.description(),
                newExpenseGroupRequestDTO.icon()
        );
        Participate newParticipate = new Participate(authUser, newExpenseGroup);

        newExpenseGroup.addParticipate(newParticipate);

        expenseGroupRepository.save(newExpenseGroup);
        participateRepository.save(newParticipate);

        return ExpenseGroupResponseDTO.of(newExpenseGroup);
    }

    @Override
    @Transactional
    public ExpenseGroupResponseDTO addUserToExpenseGroup(UserEntity authUser, Long expenseGroupId) {
        boolean existsParticipation = participateRepository.existsByUserIdAndExpenseGroupId(
                authUser.getId(),
                expenseGroupId);

        if (existsParticipation) {
            throw new ParticipationAlreadyExistsException(Long.toString(
                    authUser.getId()),
                    Long.toString(expenseGroupId));
        }

        ExpenseGroup expenseGroup = expenseGroupRepository.findById(expenseGroupId).orElseThrow(
                () -> new ExpenseGroupNotFoundException(Long.toString(expenseGroupId)));
        Participate newParticipate = new Participate(authUser, expenseGroup);

        participateRepository.save(newParticipate);
        expenseGroup.addParticipate(newParticipate);

        return ExpenseGroupResponseDTO.of(expenseGroup);
    }

    @Override
    @Transactional
    public void leaveCurrentUserFromExpenseGroup(UserEntity authUser, Long expenseGroupId) {
        participateRepository.deleteByUserIdAndExpenseGroupId(authUser.getId(), expenseGroupId);
        participateRepository.flush();
        checkAndDeleteExpenseGroup(expenseGroupId);
    }

    @Override
    @Transactional
    public ExpenseGroupResponseDTO updateExpenseGroup(Long expenseGroupId,
                                                      UpdateExpenseGroupRequestDTO updateExpenseGroupRequestDTO) {
        ExpenseGroup expenseGroupToUpdate = expenseGroupRepository.findById(expenseGroupId)
                .orElseThrow(() -> new ExpenseGroupNotFoundException(Long.toString(expenseGroupId)));

        if (updateExpenseGroupRequestDTO.name() != null) {
            CheckUtils.isValidSize(20, updateExpenseGroupRequestDTO.name());
            expenseGroupToUpdate.setName(updateExpenseGroupRequestDTO.name());
        }

        if (updateExpenseGroupRequestDTO.description() != null) {
            CheckUtils.isValidSize(200, updateExpenseGroupRequestDTO.description());
            expenseGroupToUpdate.setDescription(updateExpenseGroupRequestDTO.description());
        }

        if (updateExpenseGroupRequestDTO.icon() != null) {
            CheckUtils.isValidSize(300, updateExpenseGroupRequestDTO.icon());
            expenseGroupToUpdate.setIcon(updateExpenseGroupRequestDTO.icon());
        }

        return ExpenseGroupResponseDTO.of(expenseGroupToUpdate);
    }

    private void checkAndDeleteExpenseGroup(Long expenseGroupId) {
        ExpenseGroup expenseGroupToCheck = expenseGroupRepository.findById(expenseGroupId)
                .orElseThrow(() -> new ExpenseGroupNotFoundException(Long.toString(expenseGroupId)));

        if (expenseGroupToCheck.getParticipates().isEmpty()) {
            expenseGroupRepository.delete(expenseGroupToCheck);
        }
    }

}


