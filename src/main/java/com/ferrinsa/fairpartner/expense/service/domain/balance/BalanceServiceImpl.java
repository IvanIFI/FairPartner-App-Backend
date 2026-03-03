package com.ferrinsa.fairpartner.expense.service.domain.balance;

import com.ferrinsa.fairpartner.exception.balance.ExpenseShareIntegrityException;
import com.ferrinsa.fairpartner.exception.balance.UnsupportedParticipantsNumberException;
import com.ferrinsa.fairpartner.expense.repository.*;
import com.ferrinsa.fairpartner.expense.service.domain.balance.model.UserBalanceResult;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain service that calculates user balances within an ExpenseGroup.
 * This implementation supports groups with 1 or 2 participants.
 * It only performs calculation and validation.
 * Any decision based on the calculated balances is handled by the coordinator layer.
 */
@Service
public class BalanceServiceImpl implements BalanceService {

    private static final int MIN_USERS_SERVICE = 1;
    private static final int MAX_USERS_SERVICE = 2;

    private final ExpenseRepository expenseRepository;
    private final ExpenseShareRepository expenseShareRepository;
    private final PaymentRepository paymentRepository;
    private final ParticipateRepository participateRepository;

    @Autowired
    public BalanceServiceImpl(ExpenseRepository expenseRepository,
                              ExpenseShareRepository expenseShareRepository,
                              PaymentRepository paymentRepository,
                              ParticipateRepository participateRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseShareRepository = expenseShareRepository;
        this.paymentRepository = paymentRepository;
        this.participateRepository = participateRepository;
    }

    @Override
    public List<UserBalanceResult> obtainUsersBalance(Long expenseGroupId) {
        List<UserBalanceResult> balancesList = new ArrayList<>();
        List<UserEntity> userList = this.validateAndObtainUsersByGroupId(expenseGroupId);

        this.validateAmountConsistency(expenseGroupId);

        if (userList.size() == 1) {
            balancesList = this.calculateBalanceForOneMember(userList, expenseGroupId);
        } else if (userList.size() == 2) {
            balancesList = this.calculateBalanceForTwoMembers(userList, expenseGroupId);
        }

        return balancesList;
    }

    private List<UserBalanceResult> calculateBalanceForOneMember(List<UserEntity> userList, Long expenseGroupId) {
        UserEntity user = userList.get(0);

        BigDecimal totalAmountUser = obtainTotalAmountByUser(user.getId(), expenseGroupId);
        BigDecimal totalPaymentsUser = obtainTotalPaymentByUser(user.getId(), expenseGroupId);
        BigDecimal balanceUser = totalAmountUser.subtract(totalPaymentsUser);

        return List.of(new UserBalanceResult(user, balanceUser));
    }

    private List<UserBalanceResult> calculateBalanceForTwoMembers(List<UserEntity> userList, Long expenseGroupId) {
        UserEntity userA = userList.get(0);
        UserEntity userB = userList.get(1);

        BigDecimal totalAmountUserA = obtainTotalAmountByUser(userA.getId(), expenseGroupId);
        BigDecimal totalAmountUserB = obtainTotalAmountByUser(userB.getId(), expenseGroupId);

        BigDecimal totalPaymentsUserA = obtainTotalPaymentByUser(userA.getId(), expenseGroupId);
        BigDecimal totalPaymentsUserB = obtainTotalPaymentByUser(userB.getId(), expenseGroupId);

        BigDecimal balanceUserA = totalAmountUserA.subtract(totalPaymentsUserA);
        BigDecimal balanceUserB = totalAmountUserB.subtract(totalPaymentsUserB);

        return this.validateAndObtainBalanceResultForTwo(userA, balanceUserA, userB, balanceUserB);
    }

    private BigDecimal obtainTotalAmountByUser(Long userId, Long expenseGroupId) {
        return expenseShareRepository.sumAmountByUserAndGroup(userId, expenseGroupId);
    }

    private BigDecimal obtainTotalPaymentByUser(Long userId, Long expenseGroupId) {
        return paymentRepository.sumAmountByUserAndGroup(userId, expenseGroupId);
    }

    private BigDecimal obtainTotalAmountExpenseByExpenseGroup(Long expenseGroupId) {
        return expenseRepository.sumTotalAmountsByGroup(expenseGroupId);
    }

    private BigDecimal obtainTotalAmountSharedByExpenseGroup(Long expenseGroupId) {
        return expenseShareRepository.sumAmountExpenseShareByGroup(expenseGroupId);
    }

    private void validateAmountConsistency(Long expenseGroupId) {
        BigDecimal totalExpensesAmount = this.obtainTotalAmountExpenseByExpenseGroup(expenseGroupId);
        BigDecimal totalUsersAmount = this.obtainTotalAmountSharedByExpenseGroup(expenseGroupId);

        if (totalExpensesAmount.compareTo(totalUsersAmount) != 0) {
            throw new ExpenseShareIntegrityException();
        }
    }

    private List<UserEntity> validateAndObtainUsersByGroupId(Long expenseGroupId) {
        List<UserEntity> usersList = participateRepository.findUsersByExpenseGroupId(expenseGroupId);
        int size = usersList.size();

        if (size != MIN_USERS_SERVICE && size != MAX_USERS_SERVICE) {
            throw new UnsupportedParticipantsNumberException(String.valueOf(size));
        }

        return usersList;
    }

    private List<UserBalanceResult> validateAndObtainBalanceResultForTwo(UserEntity userA,
                                                                         BigDecimal balanceA,
                                                                         UserEntity userB,
                                                                         BigDecimal balanceB) {
        if (balanceA.add(balanceB).compareTo(BigDecimal.ZERO) != 0) {
            throw new ExpenseShareIntegrityException();
        }

        return List.of(
                new UserBalanceResult(userA, balanceA),
                new UserBalanceResult(userB, balanceB));
    }

}

