package com.ferrinsa.fairpartner.expense.service.domain.impl;

import com.ferrinsa.fairpartner.exception.expense.expensegroup.UserNotMemberOfGroupException;
import com.ferrinsa.fairpartner.exception.expense.settlement.*;
import com.ferrinsa.fairpartner.expense.dto.settlement.NewSettlementRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.settlement.SettlementResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.settlement.UpdateSettlementRequestDTO;
import com.ferrinsa.fairpartner.expense.model.Settlement;
import com.ferrinsa.fairpartner.expense.repository.ExpenseGroupRepository;
import com.ferrinsa.fairpartner.expense.repository.ParticipateRepository;
import com.ferrinsa.fairpartner.expense.repository.SettlementRepository;
import com.ferrinsa.fairpartner.expense.service.domain.SettlementService;
import com.ferrinsa.fairpartner.expense.service.domain.balance.BalanceService;
import com.ferrinsa.fairpartner.expense.service.domain.balance.model.UserBalanceResult;
import com.ferrinsa.fairpartner.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SettlementServiceImpl implements SettlementService {

    private final SettlementRepository settlementRepository;
    private final ParticipateRepository participateRepository;
    private final ExpenseGroupRepository expenseGroupRepository;
    private final UserRepository userRepository;
    private final BalanceService balanceService;

    @Autowired
    public SettlementServiceImpl(SettlementRepository settlementRepository,
                                 ParticipateRepository participateRepository,
                                 ExpenseGroupRepository expenseGroupRepository,
                                 UserRepository userRepository,
                                 BalanceService balanceService) {
        this.settlementRepository = settlementRepository;
        this.participateRepository = participateRepository;
        this.expenseGroupRepository = expenseGroupRepository;
        this.userRepository = userRepository;
        this.balanceService = balanceService;
    }

    @Override
    @Transactional
    public SettlementResponseDTO createSettlement(Long userId, NewSettlementRequestDTO newSettlementRequestDTO) {
        this.validateParticipationUsers(userId,
                newSettlementRequestDTO.toUserId(),
                newSettlementRequestDTO.expenseGroupId());
        this.validateNewSettlementAgainstBalance(userId,
                newSettlementRequestDTO.expenseGroupId(),
                newSettlementRequestDTO.amount());

        Settlement settlement = new Settlement(
                expenseGroupRepository.getReferenceById(newSettlementRequestDTO.expenseGroupId()),
                userRepository.getReferenceById(userId),
                userRepository.getReferenceById(newSettlementRequestDTO.toUserId()),
                newSettlementRequestDTO.amount()
        );
        settlementRepository.save(settlement);

        return SettlementResponseDTO.of(settlement);
    }

    @Override
    @Transactional
    public SettlementResponseDTO updateSettlement(Long userId,
                                                  Long settlementId,
                                                  UpdateSettlementRequestDTO updateSettlementRequestDTO) {
        Settlement settlementToUpdate = settlementRepository.findById(settlementId)
                .orElseThrow(() -> new SettlementNotFoundException(String.valueOf(settlementId)));

        if (!settlementToUpdate.getFromUser().getId().equals(userId)) {
            throw new SettlementOwnershipException();
        }

        this.validateSettlementUpdateAgainstBalance(userId,
                settlementToUpdate.getExpenseGroup().getId(),
                settlementToUpdate.getAmount(),
                updateSettlementRequestDTO.amount());

        settlementToUpdate.setAmount(updateSettlementRequestDTO.amount());

        return SettlementResponseDTO.of(settlementToUpdate);
    }

    @Override
    @Transactional
    public void deleteSettlement(Long userId, Long settlementId) {

        Settlement settlementToDelete = settlementRepository.findById(settlementId)
                .orElseThrow(() -> new SettlementNotFoundException(String.valueOf(settlementId)));

        if (!settlementToDelete.getFromUser().getId().equals(userId)) {
            throw new SettlementOwnershipException();
        }

        settlementRepository.delete(settlementToDelete);
    }

    @Override
    public SettlementResponseDTO findSettlementById(Long userId, Long settlementId) {
        Settlement settlement = settlementRepository.findById(settlementId)
                .orElseThrow(() ->
                        new SettlementNotFoundException(String.valueOf(settlementId)));

        if (!participateRepository.existsByUserIdAndExpenseGroupId(userId, settlement.getExpenseGroup().getId())) {
            throw new UserNotMemberOfGroupException(
                    String.valueOf(userId),
                    String.valueOf(settlement.getExpenseGroup().getId()));
        }

        return SettlementResponseDTO.of(settlement);
    }

    private void validateParticipationUsers(Long fromUserId, Long toUserId, Long expenseGroupId) {
        if (!participateRepository.existsByUserIdAndExpenseGroupId(fromUserId, expenseGroupId)) {
            throw new UserNotMemberOfGroupException(
                    String.valueOf(fromUserId),
                    String.valueOf(expenseGroupId));
        }

        if (!participateRepository.existsByUserIdAndExpenseGroupId(toUserId, expenseGroupId)) {
            throw new UserNotMemberOfGroupException(
                    String.valueOf(toUserId),
                    String.valueOf(expenseGroupId));
        }

        if (participateRepository.countByExpenseGroup_Id(expenseGroupId) == 1) {
            throw new OnlyOneMemberException();
        }

        if (fromUserId.equals(toUserId)) {
            throw new SelfSettlementNotAllowedException();
        }

    }

    private void validateNewSettlementAgainstBalance(Long userId, Long expenseGroupId, BigDecimal settlementAmount) {
        List<UserBalanceResult> usersBalanceList = balanceService.obtainUsersBalance(expenseGroupId);
        for (UserBalanceResult userBalance : usersBalanceList) {
            if (userBalance.user().getId().equals(userId)) {
                this.validateUserIsDebtor(userBalance.balance());
                this.validateSettlementNotExceedingDebt(userBalance, settlementAmount);
                return;
            }
        }

        throw new UserNotMemberOfGroupException(String.valueOf(userId), String.valueOf(expenseGroupId));
    }

    private void validateSettlementUpdateAgainstBalance(Long userId,
                                                        Long expenseGroupId,
                                                        BigDecimal previousSettlementAmount,
                                                        BigDecimal newSettlementAmount) {
        List<UserBalanceResult> usersBalanceList = balanceService.obtainUsersBalance(expenseGroupId);
        for (UserBalanceResult userBalance : usersBalanceList) {
            if (userBalance.user().getId().equals(userId)) {
                BigDecimal currentBalance = userBalance.balance();
                this.validateUserIsDebtor(currentBalance);

                /* The current balance is already reduced by the previous settlement.
                Add it back to calculate the maximum allowed amount for the update. */
                this.validateSettlementUpdatePreviousDebt(
                        currentBalance,
                        previousSettlementAmount,
                        newSettlementAmount);
                return;
            }
        }

        throw new UserNotMemberOfGroupException(String.valueOf(userId), String.valueOf(expenseGroupId));
    }

    private void validateSettlementNotExceedingDebt(UserBalanceResult userBalance, BigDecimal settlementAmount) {
        BigDecimal positiveDebt = userBalance.balance().abs();
        if (settlementAmount.compareTo(positiveDebt) > 0) {
            throw new DebtLimitExceededException();
        }
    }

    private void validateSettlementUpdatePreviousDebt(BigDecimal currentBalance,
                                                      BigDecimal previousSettlementAmount,
                                                      BigDecimal newSettlementAmount) {
        BigDecimal availableDebt = currentBalance.abs().add(previousSettlementAmount);
        if (newSettlementAmount.compareTo(availableDebt) > 0) {
            throw new DebtLimitExceededException();
        }
    }

    private void validateUserIsDebtor(BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) > 0) {
            throw new NotUserDebtorException();
        } else if (balance.compareTo(BigDecimal.ZERO) == 0) {
            throw new NotDebtException();
        }
    }

}
