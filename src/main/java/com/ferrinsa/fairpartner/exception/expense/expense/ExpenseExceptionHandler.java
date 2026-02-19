package com.ferrinsa.fairpartner.exception.expense.expense;

import com.ferrinsa.fairpartner.exception.expense.expensegroup.ExpenseGroupAccessDeniedException;
import com.ferrinsa.fairpartner.exception.expense.expensegroup.ExpenseGroupNotFoundException;
import com.ferrinsa.fairpartner.exception.expense.expensegroup.ParticipationAlreadyExistsException;
import com.ferrinsa.fairpartner.exception.expense.expensegroup.UserNotMemberOfGroupException;
import com.ferrinsa.fairpartner.exception.expense.invitation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class ExpenseExceptionHandler {

    private static final String ERROR_TYPE_BASE_URI = "https://ferrinsa.api/errors/expense/";

    private static final String TITLE_EXPENSE_NOT_FOUND = "Gasto no encontrado";

    @ExceptionHandler(ExpenseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleExpenseNotFoundException(ExpenseNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle(TITLE_EXPENSE_NOT_FOUND);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }


}
