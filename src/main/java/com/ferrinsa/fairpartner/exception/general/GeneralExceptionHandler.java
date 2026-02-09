package com.ferrinsa.fairpartner.exception.general;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;

public class GeneralExceptionHandler {

    private static final String ERROR_TYPE_BASE_URI = "https://ferrinsa.api/errors/general/";

    private static final String TITLE_INVALID_PARAMETER = "Parámetro incorrecto";

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleInvalidRequestException(InvalidRequestException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(TITLE_INVALID_PARAMETER);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }
}
