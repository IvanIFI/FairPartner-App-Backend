package com.ferrinsa.fairpartner.exception.expense.expensegroup;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class ExpenseGroupExceptionHandler {

    private static final String ERROR_TYPE_BASE_URI = "https://ferrinsa.api/errors/expense-group/";

    private static final String TITLE_EXPENSE_GROUP_NOT_FOUND = "Grupo de gastos no encontrado";
    private static final String TITLE_PARTICIPATION_ALREADY_EXISTS = "Usuario ya existe en el grupo";
    private static final String TITLE_ACCESS_DENIED_EXPENSE_GROUP = "Usuario sin acceso al grupo";
    private static final String TITLE_USER_NOT_MEMBER_GROUP = "Usuario no pertenece al grupo";

    @ExceptionHandler(ExpenseGroupNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleExpenseGroupNotFoundException(ExpenseGroupNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle(TITLE_EXPENSE_GROUP_NOT_FOUND);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(ParticipationAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleParticipationAlreadyExistsException(ParticipationAlreadyExistsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_PARTICIPATION_ALREADY_EXISTS);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(ExpenseGroupAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ProblemDetail handleExpenseGroupAccessDeniedException(ExpenseGroupAccessDeniedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problemDetail.setTitle(TITLE_ACCESS_DENIED_EXPENSE_GROUP);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(UserNotMemberOfGroupException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ProblemDetail handleUserNotMemberOfGroupException(UserNotMemberOfGroupException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problemDetail.setTitle(TITLE_USER_NOT_MEMBER_GROUP);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

}
