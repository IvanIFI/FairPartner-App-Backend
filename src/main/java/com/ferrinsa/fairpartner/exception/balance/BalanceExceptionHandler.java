package com.ferrinsa.fairpartner.exception.balance;

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


    @ExceptionHandler(UnsupportedParticipantsNumberException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleUnsupportedParticipantsNumberException(UnsupportedParticipantsNumberException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_UNSUPPORTED_PARTICIPANTS_NUMBER);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

}
