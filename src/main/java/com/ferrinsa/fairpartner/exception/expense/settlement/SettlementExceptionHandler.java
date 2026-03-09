package com.ferrinsa.fairpartner.exception.expense.settlement;

import com.ferrinsa.fairpartner.exception.expenseshare.ExpenseShareNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;

public class SettlementExceptionHandler {

    private static final String ERROR_TYPE_BASE_URI = "https://ferrinsa.api/errors/settlement/";

    private static final String TITLE_SETTLEMENT_NOT_FOUND = "Liquidación no encontrada";
    private static final String TITLE_ONLY_ONE_MEMBER = "Único miembro en el grupo";
    private static final String TITLE_DEBT_LIMIT_SETTLEMENT_EXCEEDED = "Liquidación superior a la deuda";
    private static final String TITLE_USER_IS_NOT_DEBTOR = "El usuario no es el deudor";
    private static final String TITLE_NOT_DEBT = "No existe deuda";
    private static final String TITLE_SELF_SETTLEMENT_NOT_ALLOWED = "Mismo usuario receptor y liquidador.";
    private static final String TITLE_SETTLEMENT_OWNERSHIP = "Liquidación no pertenece al usuario.";

    @ExceptionHandler(SettlementNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleSettlementNotFoundException(SettlementNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle(TITLE_SETTLEMENT_NOT_FOUND);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(OnlyOneMemberException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleOnlyOneMemberException(OnlyOneMemberException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_ONLY_ONE_MEMBER);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(DebtLimitExceededException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleDebtLimitExceededException(DebtLimitExceededException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_DEBT_LIMIT_SETTLEMENT_EXCEEDED);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(NotUserDebtorException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleNotUserDebtorException(NotUserDebtorException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_USER_IS_NOT_DEBTOR);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(NotDebtException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleNotDebtException(NotDebtException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_NOT_DEBT);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(SelfSettlementNotAllowedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleSelfSettlementNotAllowedException(SelfSettlementNotAllowedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_SELF_SETTLEMENT_NOT_ALLOWED);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(SettlementOwnershipException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleSettlementOwnershipException(SettlementOwnershipException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_SETTLEMENT_OWNERSHIP);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

}
