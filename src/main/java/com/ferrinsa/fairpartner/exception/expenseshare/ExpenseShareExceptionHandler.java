package com.ferrinsa.fairpartner.exception.expenseshare;

import com.ferrinsa.fairpartner.exception.expense.expense.ExpenseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class ExpenseShareExceptionHandler {

    private static final String ERROR_TYPE_BASE_URI = "https://ferrinsa.api/errors/expense-share/";

    private static final String TITLE_EXPENSE_SHARE_NOT_FOUND = "Gasto compartido no encontrado";
    private static final String TITLE_DUPLICATE_USERS = "Usuarios duplicados";
    private static final String TITLE_AMOUNT_INTEGRITY_ERROR = "Inconsistencia en las cantidades";
    private static final String TITLE_PAYER_NOT_IN_SHARES = "Inconsistencia en las cantidades";


    @ExceptionHandler(ExpenseShareNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleExpenseShareNotFoundException(ExpenseShareNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle(TITLE_EXPENSE_SHARE_NOT_FOUND);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(DuplicateUsersException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleDuplicateUsersException(DuplicateUsersException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_DUPLICATE_USERS);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(ExpenseShareIntegrityException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleExpenseShareIntegrityException(ExpenseShareIntegrityException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setTitle(TITLE_AMOUNT_INTEGRITY_ERROR);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(PayerNotInSharesException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handlePayerNotInSharesException(PayerNotInSharesException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_PAYER_NOT_IN_SHARES);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

}
