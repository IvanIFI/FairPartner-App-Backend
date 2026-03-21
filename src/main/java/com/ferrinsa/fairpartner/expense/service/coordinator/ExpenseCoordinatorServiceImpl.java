package com.ferrinsa.fairpartner.expense.service.coordinator;

import com.ferrinsa.fairpartner.category.model.CategoryEntity;
import com.ferrinsa.fairpartner.category.service.CategoryEntityService;
import com.ferrinsa.fairpartner.exception.expense.expense.UserNotExpenseOwnerException;
import com.ferrinsa.fairpartner.exception.expenseshare.ExpenseShareIntegrityException;
import com.ferrinsa.fairpartner.exception.expense.expensegroup.ExpenseGroupAccessDeniedException;
import com.ferrinsa.fairpartner.exception.expense.expensegroup.UserNotMemberOfGroupException;
import com.ferrinsa.fairpartner.exception.expenseshare.DuplicateUsersException;
import com.ferrinsa.fairpartner.exception.expenseshare.PayerNotInSharesException;
import com.ferrinsa.fairpartner.expense.dto.expense.*;
import com.ferrinsa.fairpartner.expense.model.Expense;
import com.ferrinsa.fairpartner.expense.model.ExpenseGroup;
import com.ferrinsa.fairpartner.expense.model.ExpenseShare;
import com.ferrinsa.fairpartner.expense.model.Payment;
import com.ferrinsa.fairpartner.expense.service.coordinator.model.ExpensesWithBalances;
import com.ferrinsa.fairpartner.expense.service.domain.ExpenseGroupService;
import com.ferrinsa.fairpartner.expense.service.domain.balance.BalanceService;
import com.ferrinsa.fairpartner.expense.service.domain.balance.model.UserBalanceResult;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.expense.repository.ParticipateRepository;
import com.ferrinsa.fairpartner.expense.service.coordinator.model.ExpenseShareRequest;
import com.ferrinsa.fairpartner.expense.service.domain.ExpenseService;
import com.ferrinsa.fairpartner.expense.service.domain.ExpenseShareService;
import com.ferrinsa.fairpartner.expense.service.domain.PaymentService;
import com.ferrinsa.fairpartner.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExpenseCoordinatorServiceImpl implements ExpenseCoordinatorService {

    private final UserService userService;
    private final ExpenseService expenseService;
    private final PaymentService paymentService;
    private final ExpenseShareService expenseShareService;
    private final ExpenseGroupService expenseGroupService;
    private final BalanceService balanceService;
    private final CategoryEntityService categoryEntityService;
    private final ParticipateRepository participateRepository;

    @Autowired
    public ExpenseCoordinatorServiceImpl(UserService userService,
                                         ExpenseService expenseService,
                                         PaymentService paymentService,
                                         ExpenseShareService expenseShareService,
                                         ExpenseGroupService expenseGroupService,
                                         BalanceService balanceService,
                                         CategoryEntityService categoryEntityService,
                                         ParticipateRepository participateRepository) {
        this.userService = userService;
        this.expenseService = expenseService;
        this.paymentService = paymentService;
        this.expenseShareService = expenseShareService;
        this.expenseGroupService = expenseGroupService;
        this.balanceService = balanceService;
        this.categoryEntityService = categoryEntityService;
        this.participateRepository = participateRepository;
    }

    @Override
    @Transactional
    public Expense createExpense(Long authUserID, CreateExpenseRequestDTO createExpenseRequestDTO) {
        this.validateCreateRequestDto(authUserID, createExpenseRequestDTO);

        ExpenseGroup expenseGroup = expenseGroupService.findExpenseGroupById(
                authUserID,
                createExpenseRequestDTO.expenseGroupId());
        // For MVP purposes, the category is fixed to DEFAULT.
        // In future implementations, category should be selected dynamically using findCategoryById.
        CategoryEntity category = categoryEntityService.getDefaultCategory();
        UserEntity creatorUser = userService.findUserById(authUserID);
        UserEntity payerUser = userService.findUserById(createExpenseRequestDTO.payerUserId());

        Expense expenseCreated = new Expense(
                expenseGroup,
                category,
                creatorUser,
                createExpenseRequestDTO.name(),
                createExpenseRequestDTO.description(),
                createExpenseRequestDTO.amount());
        expenseService.createExpense(expenseCreated);

        this.createExpenseShares(expenseCreated, createExpenseRequestDTO.expenseShares());

        Payment payment = new Payment(payerUser, expenseCreated, createExpenseRequestDTO.amount());
        paymentService.createPayment(payment);

        return expenseCreated;
    }

    @Override
    public Expense getExpenseDetails(Long authUserId, Long expenseId) {
        Expense expense = expenseService.findById(expenseId);

        if (!participateRepository.existsByUserIdAndExpenseGroupId(authUserId, expense.getExpenseGroup().getId())) {
            throw new UserNotMemberOfGroupException(
                    String.valueOf(authUserId),
                    String.valueOf(expense.getExpenseGroup().getId()));
        }

        return expense;
    }

    @Override
    public ExpensesWithBalances getListExpenses(Long authUserId, Long expenseGroupId) {
        if (!participateRepository.existsByUserIdAndExpenseGroupId(authUserId, expenseGroupId)) {
            throw new UserNotMemberOfGroupException(
                    String.valueOf(authUserId),
                    String.valueOf(expenseGroupId));
        }

        List<Expense> expensesList = expenseService.findByGroupId(expenseGroupId);
        List<UserBalanceResult> balancesList = balanceService.obtainUsersBalance(expenseGroupId);

        return new ExpensesWithBalances(expensesList, balancesList);
    }

    @Override
    @Transactional
    public void deleteExpense(Long authUserId, Long expenseId) {
        Expense expenseToDelete = expenseService.findById(expenseId);
        this.validateUserIsCreator(authUserId, expenseToDelete);
        expenseService.deleteExpense(expenseToDelete.getId());
    }

    @Override
    @Transactional
    public Expense updateExpense(Long authUserId,
                                                   Long expenseId,
                                                   UpdateExpenseRequestDTO updateExpenseRequestDTO) {
        Expense expenseToUpdate = expenseService.findById(expenseId);
        this.validateUpdateRequestDTO(authUserId, expenseToUpdate, updateExpenseRequestDTO);

        // For MVP purposes, the category is fixed to DEFAULT.
        // In future implementations, category should be selected dynamically using findCategoryById.
        CategoryEntity categoryToUpdate = categoryEntityService.getDefaultCategory();
        expenseToUpdate.setCategory(categoryToUpdate);
        expenseToUpdate.setName(updateExpenseRequestDTO.name());
        expenseToUpdate.setDescription(updateExpenseRequestDTO.description());
        expenseToUpdate.setAmount(updateExpenseRequestDTO.amount());

        UserEntity payerUserToUpdate = userService.findUserById(updateExpenseRequestDTO.payerUserId());
        paymentService.deletePayment(expenseId);
        Payment paymentUpdated = new Payment(payerUserToUpdate, expenseToUpdate, updateExpenseRequestDTO.amount());
        paymentService.createPayment(paymentUpdated);

        expenseShareService.deleteByExpenseId(expenseId);
        this.createExpenseShares(expenseToUpdate, updateExpenseRequestDTO.expenseShares());

        return expenseToUpdate;
    }

    private void validateCreateRequestDto(Long authUserId, CreateExpenseRequestDTO createExpenseRequestDTO) {
        this.validateParticipatesUsers(
                createExpenseRequestDTO.expenseGroupId(),
                authUserId,
                createExpenseRequestDTO.payerUserId(),
                createExpenseRequestDTO.expenseShares());
        if (createExpenseRequestDTO.expenseShares().size() == 1) {
            this.validateSingleUserCase(
                    createExpenseRequestDTO.payerUserId(),
                    createExpenseRequestDTO.amount(),
                    createExpenseRequestDTO.expenseShares());
        }
        this.validatePayerInShares(createExpenseRequestDTO.payerUserId(), createExpenseRequestDTO.expenseShares());
        this.validateDuplicateUsersExpenseShare(createExpenseRequestDTO.expenseShares());
        this.validateAmountShared(createExpenseRequestDTO.amount(), createExpenseRequestDTO.expenseShares());
    }

    private void validateUpdateRequestDTO(Long authUserId,
                                          Expense expense,
                                          UpdateExpenseRequestDTO updateExpenseRequestDTO) {
        this.validateUserIsCreator(authUserId, expense);
        this.validateParticipatesUsers(
                expense.getExpenseGroup().getId(),
                authUserId,
                updateExpenseRequestDTO.payerUserId(),
                updateExpenseRequestDTO.expenseShares());
        if (updateExpenseRequestDTO.expenseShares().size() == 1) {
            this.validateSingleUserCase(
                    updateExpenseRequestDTO.payerUserId(),
                    updateExpenseRequestDTO.amount(),
                    updateExpenseRequestDTO.expenseShares());
        }
        this.validatePayerInShares(updateExpenseRequestDTO.payerUserId(), updateExpenseRequestDTO.expenseShares());
        this.validateDuplicateUsersExpenseShare(updateExpenseRequestDTO.expenseShares());
        this.validateAmountShared(updateExpenseRequestDTO.amount(), updateExpenseRequestDTO.expenseShares());
    }

    private void validateParticipatesUsers(Long expenseGroupId,
                                           Long creatorUserId,
                                           Long payerUserId,
                                           List<ExpenseShareRequest> usersExpenseShares) {
        Set<Long> groupUserIds = participateRepository
                .findUsersByExpenseGroupId(expenseGroupId)
                .stream()
                .map(UserEntity::getId)
                .collect(Collectors.toSet());

        if (!groupUserIds.contains(creatorUserId)) {
            throw new ExpenseGroupAccessDeniedException(String.valueOf(creatorUserId), String.valueOf(expenseGroupId));
        }

        if (!groupUserIds.contains(payerUserId)) {
            throw new UserNotMemberOfGroupException(String.valueOf(payerUserId), String.valueOf(expenseGroupId));
        }

        for (ExpenseShareRequest share : usersExpenseShares) {
            if (!groupUserIds.contains(share.userId())) {
                throw new UserNotMemberOfGroupException(
                        String.valueOf((share.userId())),
                        String.valueOf(expenseGroupId));
            }
        }
    }

    private void validateAmountShared(BigDecimal amount, List<ExpenseShareRequest> usersExpenseShares) {
        BigDecimal sumShared = usersExpenseShares.stream()
                .map(ExpenseShareRequest::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sumShared.compareTo(amount) != 0) {
            throw new ExpenseShareIntegrityException();
        }
    }

    private void validateDuplicateUsersExpenseShare(List<ExpenseShareRequest> usersExpenseShares) {
        long distinctIds = usersExpenseShares.stream().map(ExpenseShareRequest::userId).distinct().count();

        if (distinctIds != usersExpenseShares.size()) {
            throw new DuplicateUsersException();
        }
    }

    private void validatePayerInShares(Long payerId, List<ExpenseShareRequest> usersExpenseShares) {
        boolean payerIncluded = usersExpenseShares.stream()
                .anyMatch(share -> share.userId().equals(payerId));

        if (!payerIncluded) {
            throw new PayerNotInSharesException();
        }
    }

    private void validateSingleUserCase(Long payerUserId,
                                        BigDecimal amount,
                                        List<ExpenseShareRequest> usersExpenseShares) {
        ExpenseShareRequest share = usersExpenseShares.get(0);

        if (!share.userId().equals(payerUserId)) {
            throw new PayerNotInSharesException();
        }

        if (share.amount().compareTo(amount) != 0) {
            throw new ExpenseShareIntegrityException();
        }
    }

    private void validateUserIsCreator(Long authUserId, Expense expense) {
        if (!authUserId.equals(expense.getCreatedBy().getId())) {
            throw new UserNotExpenseOwnerException(
                    String.valueOf(authUserId),
                    String.valueOf(expense.getId()));
        }
    }

    private void createExpenseShares(Expense expenseCreated, List<ExpenseShareRequest> expenseShares) {
        for (ExpenseShareRequest expenseShareDto : expenseShares) {
            UserEntity user = userService.findUserById(expenseShareDto.userId());

            ExpenseShare expenseShare = new ExpenseShare(user, expenseCreated, expenseShareDto.amount());
            expenseShareService.createExpenseShare(expenseShare);
        }
    }

}