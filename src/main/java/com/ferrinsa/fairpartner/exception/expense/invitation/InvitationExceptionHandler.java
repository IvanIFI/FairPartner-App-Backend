package com.ferrinsa.fairpartner.exception.expense.invitation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class InvitationExceptionHandler {

    private static final String ERROR_TYPE_BASE_URI = "https://ferrinsa.api/errors/invitation/";

    private static final String TITLE_INVITATION_ALREADY_EXISTS = "Usuario ya invitado al grupo";
    private static final String TITLE_INVITATION_ALREADY_ACCEPTED = "Usuario ya pertenece al grupo";
    private static final String TITLE_CANNOT_CREATE_INVITATION = "No se ha podido crear la invitación";
    private static final String TITLE_SELF_INVITATION_NOT_ALLOWED = "Usuario invitado a sí mismo";
    private static final String TITLE_INVITATION_NOT_FOUND = "Invitación no encontrada";
    private static final String TITLE_USER_NOT_INVITED = "Usuario no invitado";
    private static final String TITLE_EXPIRED_INVITATION = "Invitación expirada";
    private static final String TITLE_CANCELED_INVITATION = "Invitación cancelada";
    private static final String TITLE_REJECT_INVITATION = "Invitación ya rechazada";
    private static final String TITLE_INVITATION_STATUS_NOT_MANAGED = "Estado de invitación no controlado";

    @ExceptionHandler(InvitationAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleInvitationAlreadyExistsException(InvitationAlreadyExistsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_INVITATION_ALREADY_EXISTS);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(InvitationAlreadyAcceptedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleInvitationAlreadyAcceptedException(InvitationAlreadyAcceptedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_INVITATION_ALREADY_ACCEPTED);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(InvitationCreationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleInvitationCreationException(InvitationCreationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_CANNOT_CREATE_INVITATION);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(SelfInvitationNotAllowedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleSelfInvitationNotAllowedException(SelfInvitationNotAllowedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_SELF_INVITATION_NOT_ALLOWED);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(InvitationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleInvitationNotFoundException(InvitationNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle(TITLE_INVITATION_NOT_FOUND);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(UserNotInvitedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ProblemDetail handleInvitationAcceptException(UserNotInvitedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problemDetail.setTitle(TITLE_USER_NOT_INVITED);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(ExpiredInvitationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleInvitationExpiredException(ExpiredInvitationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_EXPIRED_INVITATION);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(CanceledInvitationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleCanceledInvitationException(CanceledInvitationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_CANCELED_INVITATION);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(RejectInvitationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleRejectInvitationException(RejectInvitationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle(TITLE_REJECT_INVITATION);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

    @ExceptionHandler(InvitationStateNotManagedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleInvitationStateNotManagedException(InvitationStateNotManagedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setTitle(TITLE_INVITATION_STATUS_NOT_MANAGED);
        problemDetail.setType(URI.create(ERROR_TYPE_BASE_URI + ex.getCode()));
        return problemDetail;
    }

}
