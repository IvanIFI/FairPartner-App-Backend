package com.ferrinsa.fairpartner.exception.expense.invitation;

import com.ferrinsa.fairpartner.exception.AppException;

public class CanceledInvitationException extends AppException {

    public CanceledInvitationException() {
        super("CANCELED_INVITATION", "La invitación esta cancelada");
    }

}
