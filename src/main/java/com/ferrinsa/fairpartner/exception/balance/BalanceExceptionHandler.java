package com.ferrinsa.fairpartner.exception.balance;

import com.ferrinsa.fairpartner.exception.category.CategoryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class BalanceExceptionHandler {

    private static final String ERROR_TYPE_BASE_URI = "https://ferrinsa.api/errors/balance/";

    private static final String TITLE_UNSUPPORTED_PARTICIPANTS_NUMBER = "Numero participantes no soportado";
    private static final String TITLE_AMOUNT_INTEGRITY_ERROR = "Inconsistencia en las cantidades";


    @ExceptionHandler(UnsupportedParticipantsNumberException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleUnsupportedParticipantsNumberException(UnsupportedParticipantsNumberException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_UNSUPPORTED_PARTICIPANTS_NUMBER);
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

}
