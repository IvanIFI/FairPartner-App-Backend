package com.ferrinsa.fairpartner.expense.service.coordinator;

import com.ferrinsa.fairpartner.category.model.CategoryEntity;
import com.ferrinsa.fairpartner.category.service.CategoryEntityService;
import com.ferrinsa.fairpartner.exception.expense.expense.UserNotExpenseOwnerException;
import com.ferrinsa.fairpartner.exception.expense.expensegroup.ExpenseGroupAccessDeniedException;
import com.ferrinsa.fairpartner.exception.expense.expensegroup.UserNotMemberOfGroupException;
import com.ferrinsa.fairpartner.exception.expenseshare.DuplicateUsersException;
import com.ferrinsa.fairpartner.exception.expenseshare.ExpenseShareIntegrityException;
import com.ferrinsa.fairpartner.exception.expenseshare.PayerNotInSharesException;
import com.ferrinsa.fairpartner.expense.dto.expense.CreateExpenseRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.expense.ExpenseShareRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.expense.UpdateExpenseRequestDTO;
import com.ferrinsa.fairpartner.expense.model.Expense;
import com.ferrinsa.fairpartner.expense.model.ExpenseGroup;
import com.ferrinsa.fairpartner.expense.model.ExpenseShare;
import com.ferrinsa.fairpartner.expense.model.Payment;
import com.ferrinsa.fairpartner.expense.repository.ParticipateRepository;
import com.ferrinsa.fairpartner.expense.service.domain.ExpenseGroupService;
import com.ferrinsa.fairpartner.expense.service.domain.ExpenseService;
import com.ferrinsa.fairpartner.expense.service.domain.ExpenseShareService;
import com.ferrinsa.fairpartner.expense.service.domain.PaymentService;
import com.ferrinsa.fairpartner.expense.service.domain.balance.BalanceService;
import com.ferrinsa.fairpartner.expense.service.domain.balance.model.UserBalanceResult;
import com.ferrinsa.fairpartner.expense.service.model.*;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import com.ferrinsa.fairpartner.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    public ExpenseWithSharesAndPayer createExpense(Long authUserID, CreateExpenseRequestDTO createExpenseRequestDTO) {
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

        List<ExpenseShareRequest> expenseSharesRequestList =
                this.mapToExpenseShareRequest(createExpenseRequestDTO.expenseShares());
        List<ExpenseShare> expenseSharesList = this.createExpenseShares(expenseCreated, expenseSharesRequestList);

        Payment payment = new Payment(payerUser, expenseCreated, createExpenseRequestDTO.amount());
        paymentService.createPayment(payment);

        return new ExpenseWithSharesAndPayer(expenseCreated, expenseSharesList,payment.getUser());
    }

    @Override
    public ExpenseWithSharesAndPayer getExpenseDetails(Long authUserId, Long expenseId) {
        Expense expense = expenseService.findById(expenseId);

        if (!participateRepository.existsByUserIdAndExpenseGroupId(authUserId, expense.getExpenseGroup().getId())) {
            throw new UserNotMemberOfGroupException(
                    String.valueOf(authUserId),
                    String.valueOf(expense.getExpenseGroup().getId()));
        }

        List<ExpenseShare> expenseShares = expenseShareService.findByExpenseId(expenseId);
        Payment payment = paymentService.findByExpenseId(expenseId);

        return new ExpenseWithSharesAndPayer(expense,expenseShares,payment.getUser());
    }

    @Override
    public ExpensesWithBalances getListExpenses(Long authUserId, Long expenseGroupId) {
        if (!participateRepository.existsByUserIdAndExpenseGroupId(authUserId, expenseGroupId)) {
            throw new UserNotMemberOfGroupException(
                    String.valueOf(authUserId),
                    String.valueOf(expenseGroupId));
        }

        List<Expense> expensesList = expenseService.findByGroupId(expenseGroupId);

        List<ExpenseSummary> expensesSummaryList = new ArrayList<>();

         for (Expense expense : expensesList ) {
             //FIXME: N + 1 queries, pendiente de optimizar
             UserEntity userPayer = paymentService.findByExpenseId(expense.getId()).getUser();

             expensesSummaryList.add( new ExpenseSummary(
                     expense.getId(),
                     expense.getName(),
                     expense.getDescription(),
                     expense.getIcon(),
                     expense.getCreatedDate(),
                     expense.getAmount(),
                     new PayerResponse(userPayer.getId(),userPayer.getName())));

         }

        List<UserBalanceResult> balancesList = balanceService.obtainUsersBalance(expenseGroupId);

        return new ExpensesWithBalances(expensesSummaryList, balancesList);
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
    public ExpenseWithSharesAndPayer updateExpense(Long authUserId,
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
        List<ExpenseShareRequest> expenseSharesRequestList =
                this.mapToExpenseShareRequest(updateExpenseRequestDTO.expenseShares());
        List<ExpenseShare> expenseSharesList = this.createExpenseShares(expenseToUpdate, expenseSharesRequestList);

        return new ExpenseWithSharesAndPayer(expenseToUpdate, expenseSharesList,paymentUpdated.getUser());
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
                                           List<ExpenseShareRequestDTO> usersExpenseShares) {
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

        for (ExpenseShareRequestDTO share : usersExpenseShares) {
            if (!groupUserIds.contains(share.userId())) {
                throw new UserNotMemberOfGroupException(
                        String.valueOf((share.userId())),
                        String.valueOf(expenseGroupId));
            }
        }
    }

    private void validateAmountShared(BigDecimal amount, List<ExpenseShareRequestDTO> usersExpenseShares) {
        BigDecimal sumShared = usersExpenseShares.stream()
                .map(ExpenseShareRequestDTO::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sumShared.compareTo(amount) != 0) {
            throw new ExpenseShareIntegrityException();
        }
    }

    private void validateDuplicateUsersExpenseShare(List<ExpenseShareRequestDTO> usersExpenseShares) {
        long distinctIds = usersExpenseShares.stream().map(ExpenseShareRequestDTO::userId).distinct().count();

        if (distinctIds != usersExpenseShares.size()) {
            throw new DuplicateUsersException();
        }
    }

    private void validatePayerInShares(Long payerId, List<ExpenseShareRequestDTO> usersExpenseShares) {
        boolean payerIncluded = usersExpenseShares.stream()
                .anyMatch(share -> share.userId().equals(payerId));

        if (!payerIncluded) {
            throw new PayerNotInSharesException();
        }
    }

    private void validateSingleUserCase(Long payerUserId,
                                        BigDecimal amount,
                                        List<ExpenseShareRequestDTO> usersExpenseShares) {
        ExpenseShareRequestDTO share = usersExpenseShares.get(0);

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

    private List<ExpenseShare> createExpenseShares(Expense expenseCreated, List<ExpenseShareRequest> expenseShares) {
        List<ExpenseShare> expenseShareList = new ArrayList<>();

        for (ExpenseShareRequest expenseShareDto : expenseShares) {
            UserEntity user = userService.findUserById(expenseShareDto.userId());

            ExpenseShare expenseShare = new ExpenseShare(user, expenseCreated, expenseShareDto.amount());
            expenseShareService.createExpenseShare(expenseShare);
            expenseShareList.add(expenseShare);
        }

        return expenseShareList;
    }

    private List<ExpenseShareRequest> mapToExpenseShareRequest(List<ExpenseShareRequestDTO> dtoList) {
        return dtoList.stream()
                .map(s -> new ExpenseShareRequest(s.userId(), s.amount()))
                .toList();
    }

}