package com.ferrinsa.fairpartner.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class UserExceptionHandler {

    private static final String ERROR_TYPE_BASE_URI = "https://ferrinsa.api/errors/user/";

    private static final String TITLE_USER_NOT_FOUND = "Usuario no encontrado";
    private static final String TITLE_AUTHENTICATION_FAILED = "Autenticación fallida";
    private static final String TITLE_EMAIL_ALREADY_EXISTS = "El email ya existe";
    private static final String TITLE_INVALID_UPDATE_PARAMETERS = "Parámetros de actualización inválidos";
    private static final String TITLE_PASSWORD_CONFIRMATION_FAILED = "Confirmación de contraseña fallida";
    private static final String TITLE_INVALID_PASSWORD = "Contraseña inválida";


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleUserNotFoundException(UserNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle(TITLE_USER_NOT_FOUND);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(UserLoginFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ProblemDetail handleUserLoginFailedException(UserLoginFailedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problemDetail.setTitle(TITLE_AUTHENTICATION_FAILED);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI+ ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleUserEmailAlreadyExistsException(UserEmailAlreadyExistsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_EMAIL_ALREADY_EXISTS);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(UserFailedUpdateProfileException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleUserFailedUpdateProfileException(UserFailedUpdateProfileException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_INVALID_UPDATE_PARAMETERS);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(UserPasswordException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleUserPasswordException(UserPasswordException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_PASSWORD_CONFIRMATION_FAILED);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(UserPasswordCheckException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleUserPasswordCheckException(UserPasswordCheckException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_INVALID_PASSWORD);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

}
