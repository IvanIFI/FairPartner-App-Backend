package com.ferrinsa.fairpartner.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String TITLE_INVALID_PARAMETER = "Parámetro incorrecto";
    private static final String DETAIL_INVALID_PARAMETER = "Los datos enviados no cumplen las validaciones.";

    private static final String TITLE_INVALID_PARAMETERS = "Parámetros inválidos";
    private static final String DETAIL_INVALID_PARAMETERS = "Alguno de los parámetros no es válido.";

    private static final String TITLE_USER_NOT_FOUND = "Usuario no encontrado";
    private static final String DETAIL_USER_NOT_FOUND = "No se ha encontrado el usuario: ";

    private static final String TITLE_CONSTRAINT_VIOLATION = "Constraint violation";
    private static final String DETAIL_CONSTRAINT_VIOLATION = "Ya existe un recurso con los datos proporcionados";

    private static final String TITLE_ILLEGAL_ARGUMENT = "Illegal Argument";
    private static final String DETAIL_ILLEGAL_ARGUMENT = "Ha ocurrido un error con un parámetro";


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(TITLE_INVALID_PARAMETER);
        problemDetail.setDetail(DETAIL_INVALID_PARAMETER);
        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(TITLE_INVALID_PARAMETERS);
        problemDetail.setDetail(DETAIL_INVALID_PARAMETERS);
        return problemDetail;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle(TITLE_USER_NOT_FOUND);
        problemDetail.setDetail(DETAIL_USER_NOT_FOUND + ex.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle(TITLE_CONSTRAINT_VIOLATION);
        problemDetail.setDetail(DETAIL_CONSTRAINT_VIOLATION);
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleDIllegalArgumentException(IllegalArgumentException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(TITLE_ILLEGAL_ARGUMENT);
        problemDetail.setDetail(DETAIL_ILLEGAL_ARGUMENT);
        return problemDetail;
    }

}
