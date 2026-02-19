package com.ferrinsa.fairpartner.exception.expense.invitation;

import com.ferrinsa.fairpartner.exception.AppException;

public class RejectInvitationException extends AppException {

    public RejectInvitationException() {
        super("REJECT_INVITATION", "La invitación esta rechazada por el invitado");
    }

}
