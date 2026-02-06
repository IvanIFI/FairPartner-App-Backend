package com.ferrinsa.fairpartner.exception.expense;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class ExpenseExceptionHandler {

    private static final String ERROR_TYPE_BASE_URI = "https://ferrinsa.api/errors/expense/";

    private static final String TITLE_EXPENSE_GROUP_NOT_FOUND = "Grupo de gastos no encontrado";
    private static final String TITLE_EXPENSE_NOT_FOUND = "Gasto no encontrado";


    @ExceptionHandler(ExpenseGroupNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleExpenseGroupNotFoundException(ExpenseGroupNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle(TITLE_EXPENSE_GROUP_NOT_FOUND);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(ExpenseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleExpenseNotFoundException(ExpenseNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle(TITLE_EXPENSE_NOT_FOUND);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

}
